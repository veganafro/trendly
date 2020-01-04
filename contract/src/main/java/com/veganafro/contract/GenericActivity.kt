package com.veganafro.contract

import com.veganafro.model.NytTopic

interface GenericActivity {

    fun goToNytArticleDetails(article: NytTopic.Article)
}