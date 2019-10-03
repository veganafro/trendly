package com.veganafro.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.veganafro.model.NytTopic
import kotlinx.android.synthetic.main.nyt_trending_card.view.nyt_title_text
import kotlinx.android.synthetic.main.nyt_trending_card.view.nyt_section_text

class NytTrendingAdapter(private val articles: MutableList<NytTopic.Article>)
    : RecyclerView.Adapter<NytTrendingAdapter.NytArticleViewHolder>() {

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NytArticleViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.nyt_trending_card, parent, false)
        return NytArticleViewHolder(view)
    }

    @Suppress("ReplaceGetOrSet")
    override fun onBindViewHolder(holder: NytArticleViewHolder, position: Int) {
        holder.bind(articles.get(position))
    }

    fun updateData(articles: MutableList<NytTopic.Article>) {
        this.articles.clear()
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    class NytArticleViewHolder(private val view: View)
        : RecyclerView.ViewHolder(view) {

        fun bind(article: NytTopic.Article) {
            view.nyt_title_text.text = article.title
            view.nyt_section_text.text = article.section
        }
    }
}