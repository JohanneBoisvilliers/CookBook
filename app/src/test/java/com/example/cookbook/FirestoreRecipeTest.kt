package com.example.cookbook

import com.example.cookbook.models.Ingredient
import com.example.cookbook.models.IngredientData
import com.example.cookbook.models.IngredientDatabase
import com.example.cookbook.models.Step
import com.example.cookbook.repositories.FirestoreRecipeRepository
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.lang.reflect.Method

@RunWith(JUnit4::class)
class FirestoreRecipeTest {
    @Mock
    lateinit var firestoreRecipeRepository: FirestoreRecipeRepository
    lateinit var ingredientData : IngredientData
    lateinit var ingredientDatabase: IngredientDatabase
    lateinit var ingredient:Ingredient
    lateinit var step: Step

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        //init an ingredient
        ingredientData = IngredientData(ingredientDetailsId = 12L,recipeId = 1,ingredientDatabaseId = 1, unit = "gr", quantity = 2)
        ingredientDatabase = IngredientDatabase(0L,"sucre")
        ingredient = Ingredient(ingredientData,ingredientDatabase)
        //init a step
        step = Step(0L,0L,"","test description")
    }

    @Test
    fun testConversionIngredientIntoString(){
        val ingredientList = mutableListOf<Ingredient>()
        ingredientList.add(ingredient)
        //reflection use for accessing private method
        val privateConvertMethod: Method =
                FirestoreRecipeRepository::class.java.getDeclaredMethod("convertIngredientIntoString", MutableList::class.java)
        privateConvertMethod.isAccessible = true

        val listReturned = privateConvertMethod.invoke(firestoreRecipeRepository, ingredientList) as List<String>

        assert(listReturned is List<String>)
        assertEquals(1,listReturned.size)
        assertEquals("2 gr sucre",listReturned[0])
    }

    @Test
    fun testConversionStepIntoString(){
        val stepList = mutableListOf<Step>()
        stepList.add(step)
        //reflection use for accessing private method
        val privateConvertMethod: Method =
                FirestoreRecipeRepository::class.java.getDeclaredMethod("convertStepIntoString", MutableList::class.java)
        privateConvertMethod.isAccessible = true

        val listReturned = privateConvertMethod.invoke(firestoreRecipeRepository, stepList) as List<String>

        assert(listReturned is List<String>)
        assertEquals(1,listReturned.size)
        assertEquals("test description",listReturned[0])
    }
}