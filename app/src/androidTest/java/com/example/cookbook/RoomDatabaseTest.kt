package com.example.cookbook

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.cookbook.database.CookBookLocalDatabase
import com.example.cookbook.database.dao.RecipeDao
import org.junit.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.cookbook.database.dao.IngredientDao
import com.example.cookbook.database.dao.StepDao
import com.example.cookbook.models.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var recipeDao: RecipeDao
    private lateinit var ingredientDao: IngredientDao
    private lateinit var stepDao: StepDao
    private lateinit var db: CookBookLocalDatabase
    private lateinit var baseDataRecipe:BaseDataRecipe
    private lateinit var ingredientData:IngredientData
    private lateinit var ingredientDatabase:IngredientDatabase
    private lateinit var step : Step

    @Before
    fun setUp(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
                appContext, CookBookLocalDatabase::class.java).build()
        recipeDao = db.recipeDao()
        ingredientDao = db.ingredientDao()
        stepDao = db.stepDao()
        baseDataRecipe = initBaseDataRecipe()
        ingredientData = initIngredientData()
        ingredientDatabase = initIngredientDatabase()
        step = initStep()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndReadRecipe() {
        runBlocking {
            // before insert - check if database is empty
            recipeDao.getRecipes().observeOnceBis{
                assertEquals(0,it.size)
            }
            // insert a recipe
            recipeDao.insertRecipe(baseDataRecipe)
            // after insert - check if there is an element in database
            recipeDao.getRecipes().observeOnceBis{
                assertEquals(1,it.size)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun testRelations() {
        val ingredient2 = ingredientData.copy(ingredientDetailsId = 100001,recipeId = 2)
        val step2 = step.copy(id=2,description = "desc 2",recipeId = 1)
        val step3 = step.copy(id=3,description = "desc 3",recipeId = 2)
        runBlocking {
            // insert elements
            ingredientDao.addIngredient(ingredientData)
            ingredientDao.addIngredient(ingredient2)
            stepDao.addStep(step)
            stepDao.addStep(step2)
            stepDao.addStep(step3)
            recipeDao.insertRecipe(baseDataRecipe)
            // get recipe and check if all elements are here
            val currentRecipe = recipeDao.getSpecificRecipe(1)
            assertEquals(1,currentRecipe.ingredientList.size)
            assertEquals(100000,currentRecipe.ingredientList[0].ingredientDatabaseId)
            assertEquals(2,currentRecipe.stepList.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateAndReadRecipe(){
        runBlocking {
            recipeDao.insertRecipe(baseDataRecipe)
            val recipe = recipeDao.getSpecificRecipe(1)
            assertEquals("boulgour",recipe.baseDataRecipe?.name)
            val baseDataRecipeUpdate = recipe.baseDataRecipe?.copy(name = "change")
            recipeDao.updateRecipe(baseDataRecipeUpdate!!)
            val updatedRecipe = recipeDao.getSpecificRecipe(1)
            assertEquals("change",updatedRecipe.baseDataRecipe?.name)
        }

    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    //---------------------- PRIVATE METHODS ----------------------

    private fun initBaseDataRecipe():BaseDataRecipe{
        return BaseDataRecipe(
                addDate = "22/22/2222",
                isAlreadyDone = false,
                baseRecipeId = 1,
                recipeUrl = "",
                category = "Main",
                name = "boulgour",
                numberOfLike = 0
        )
    }

    private fun initIngredientData():IngredientData{
        return IngredientData(ingredientDatabaseId = 100000,quantity = 1, unit = "none", recipeId = 1, ingredientDetailsId = 12 )
    }

    private fun initIngredientDatabase():IngredientDatabase{
        return IngredientDatabase(100000,"Pomme")
    }

    private fun initStep():Step{
        return Step(id = 1 , recipeId = 1, photoUrl = "", description = "test description" )
    }
}

fun <T> LiveData<T>.observeOnceBis(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserverBis(handler = onChangeHandler)
    observe(observer, observer)
}