package com.example.cookbook.models

import java.util.*

data class HeadLineArticle(
        val description:String,
        val photoUrl:String,
        val url:String,
        val sharedDate: Date?
)
{
     constructor():this("","","",null)
}