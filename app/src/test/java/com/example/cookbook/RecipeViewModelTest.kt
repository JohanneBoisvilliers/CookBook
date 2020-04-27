package com.example.cookbook

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.cookbook.models.HeadLineArticle
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.models.Recipe
import com.example.cookbook.recipesPage.RecipeViewModel
import com.example.cookbook.repositories.*
import com.jraska.livedata.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    var recipesList = mutableListOf<Recipe>()
    var ingredientDatabaseList = mutableListOf<IngredientDatabase>()

    @ExperimentalCoroutinesApi
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
        this.setRecipeList()
        this.setIngredientDatabaseList()
    }

    @ExperimentalCoroutinesApi
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
        runBlocking{
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

    //------------------------- TEST INTERNAL DATA -------------------------

    @Test
    fun testGetRandomRecipes(){
        runBlocking {
            `when`(recipeRepository.getRandomRecipe()).thenReturn(
                   recipesList
            )
        }
        viewmodel.randomRecipes.test().awaitValue()
        viewmodel.randomRecipes.observeOnce {
            assertEquals(2,it.size)
        }
    }
    @Test
    fun testGetIngredientlistForPickList(){
        runBlocking {
            `when`(ingredientRepository.getIngredientList()).thenReturn(ingredientDatabaseList)
        }
        viewmodel.ingredientListPicked.test().awaitValue()
        viewmodel.ingredientListPicked.observeOnce {
            assertEquals(2,it.size)
        }
    }

    //------------------------- PRIVATE METHODS -------------------------

    private fun setRecipeList(){
        val recipe1 = Recipe()
        val recipe2 = Recipe()
        recipesList.add(recipe1)
        recipesList.add(recipe2)
    }

    private fun setIngredientDatabaseList(){
        val ingredientDatabase1 = IngredientDatabase()
        val ingredientDatabase2 = IngredientDatabase()
        ingredientDatabaseList.add(ingredientDatabase1)
        ingredientDatabaseList.add(ingredientDatabase2)
    }
}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}