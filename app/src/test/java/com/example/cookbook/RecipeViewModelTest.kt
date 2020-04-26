package com.example.cookbook

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cookbook.models.HeadLineArticle
import com.example.cookbook.recipesPage.RecipeViewModel
import com.example.cookbook.repositories.*
import com.jraska.livedata.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*


@RunWith(JUnit4::class)
class RecipeViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var recipeRepository: RecipesDataRepository
    @Mock
    lateinit var ingredientRepository: IngredientDataRepository
    @Mock
    lateinit var stepRepository: StepDataRepository
    @Mock
    lateinit var photoRepository: PhotoDataRepository
    @Mock
    lateinit var firestoreRecipeRepository: FirestoreRecipeRepository
    lateinit var viewmodel: RecipeViewModel
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
        this.viewmodel = RecipeViewModel(
                mRecipesDataRepository = recipeRepository,
                mFirestoreRecipeRepository = firestoreRecipeRepository,
                mIngredientDataRepository = ingredientRepository,
                mPhotoDataRepository = photoRepository,
                mStepDataRepository = stepRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    //------------------------- TEST EXTERNAL DATA -------------------------

    @Test
    fun testGetRecipeFromFirebase_withData() {
        runBlocking {
            `when`(firestoreRecipeRepository.getRecipes("sharedRecipes")).thenReturn(
                    mutableListOf(
                            mapOf("test" to "test"),
                            mapOf("test" to "test")
                    )
            )
            `when`(firestoreRecipeRepository.getRecipes("favorites")).thenReturn(
                    mutableListOf(
                            mapOf("test" to "test")
                    )
            )
        }
        viewmodel.getSharedRecipes()
        viewmodel.sharedRecipesList.observeOnce {
            assertEquals(2, it.size)
        }
        viewmodel.getFavoritesRecipes()
        viewmodel.favoritesRecipesList.observeOnce {
            assertEquals(1, it.size)
        }
    }
    @Test
    fun testGetRecipeFromFirebase_noData() {
        runBlocking {
            `when`(firestoreRecipeRepository.getRecipes("sharedRecipes")).thenReturn(
                    mutableListOf()
            )
            `when`(firestoreRecipeRepository.getRecipes("favorites")).thenReturn(
                    mutableListOf()
            )
        }
        viewmodel.getSharedRecipes()
        viewmodel.sharedRecipesList.observeOnce {
            assertEquals(0, it.size)
        }
        viewmodel.getFavoritesRecipes()
        viewmodel.favoritesRecipesList.observeOnce {
            assertEquals(0, it.size)
        }
    }
    @Test
    fun testGetArticleFromFirebase(){
        runBlocking (Dispatchers.IO){
            `when`(firestoreRecipeRepository.getHeadLineArticle()).thenReturn(HeadLineArticle(
                    description = "description test",
                    photoUrl = "url",
                    sharedDate = Date(12L),
                    url = "site"
            ))
        }
        viewmodel.article.observeOnce {
            assertEquals("description test",it.description)
            assertEquals("url", it.photoUrl)
            assertEquals(Date(12L),it.sharedDate)
            assertEquals("site",it.url)
        }
    }

}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}