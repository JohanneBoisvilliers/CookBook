const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();

// Listen for changes in all documents in the 'users' collection
exports.favorites = functions.region('europe-west2').firestore
  .document('sharedRecipes/{recipeId}/counter/{counterId}/shards/{shardId}')
  .onUpdate((change, context) => {
    let likedRecipeCollection = db.collection('favorites');
    let updateRecipeDocRef = db.collection('sharedRecipes').doc(context.params.recipeId);
    let updateCounterDocRef = updateRecipeDocRef.collection('counter').doc(context.params.counterId)

    updateRecipeDocRef.get()
      .then(doc => {
        if (!doc.exists) {
          console.log('No such document!');
        } else {
          likedRecipeCollection.doc(context.params.recipeId)
          .set({...doc.data()})
          .then(()=>{
              return getCount(updateCounterDocRef);
            })
            .then(number => {
              return likedRecipeCollection.doc(context.params.recipeId).set({counter: number},{merge: true});
            })
            .then(()=>{
              return filterFirst();
            });
        }
      })
      .catch(err => {
        console.log('Error getting document', err);
      });
  });

async function getCount(docRef) {
  const querySnapshot = await docRef.collection('shards').get();
  const documents = querySnapshot.docs;

  let count = 0;
  for (const doc of documents) {
    count += doc.get('count');
  }
  return count;
}

function filterFirst(){
  let first = db.collection('favorites')
  .orderBy('counter','desc')
  .limit(10);

  let paginate = first.get()
  .then((snapshot) => {
    // ...

    // Get the last document
    let last = snapshot.docs[snapshot.docs.length - 1];
    console.log(last.data().counter)

    // Construct a new query starting at this document.
    // Note: this will not have the desired effect if multiple
    // cities have the exact same population value.
    let next = db.collection('favorites')
      .orderBy('counter','desc')
      .startAfter(last.data().counter);

      deleteCollection(db,next);
    
  });

  function deleteCollection(db, queryNext) {
    let query = queryNext;
  
    return new Promise((resolve, reject) => {
      deleteQueryBatch(db, query, resolve, reject);
    });
  }
  
  function deleteQueryBatch(db, collectionRef, resolve, reject) {
    collectionRef.get()
      .then((snapshot) => {
        // When there are no documents left, we are done
        if (snapshot.size === 0) {
          return 0;
        }
  
        // Delete documents in a batch
        let batch = db.batch();
        snapshot.docs.forEach((doc) => {
          batch.delete(doc.ref);
        });
  
        return batch.commit().then(() => {
          console.log("nomber d'élément à supprimer :`${snapshot.size}`")
          return snapshot.size;
        });
      }).then((numDeleted) => {
        if (numDeleted === 0) {
          resolve();
          return;
        }
  
        // Recurse on the next process tick, to avoid
        // exploding the stack.
        process.nextTick(() => {
          deleteQueryBatch(db, query, resolve, reject);
        });
      })
      .catch(reject => {
        console.log('Error getting document', reject);
      });
  }
  
}


