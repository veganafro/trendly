package com.veganafro.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.veganafro.model.NytTopic
import kotlinx.android.synthetic.main.nyt_trending_card.view.nyt_title_text
import kotlinx.android.synthetic.main.nyt_trending_card.view.nyt_section_text

class NytTrendingAdapter constructor(
    private val onArticleClickedCallback: (article: NytTopic.Article) -> Unit
) : ListAdapter<NytTopic.Article, NytTrendingAdapter.NytArticleViewHolder>(
        NytArticleDiffCallback()
    ) {

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NytArticleViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.nyt_trending_card, parent, false)
        return NytArticleViewHolder(
            view,
            onArticleClickedCallback
        )
    }

    @Suppress("ReplaceGetOrSet")
    override fun onBindViewHolder(holder: NytArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NytArticleViewHolder(
        private val view: View,
        private val onArticleClickedCallback: (article: NytTopic.Article) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        fun bind(article: NytTopic.Article) {
            view.nyt_title_text.text = article.title
            view.nyt_section_text.text = article.section

            view.setOnClickListener {
                onArticleClickedCallback.invoke(article)
            }
        }
    }

    class NytArticleDiffCallback : DiffUtil.ItemCallback<NytTopic.Article>() {
        override fun areItemsTheSame(oldItem: NytTopic.Article, newItem: NytTopic.Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NytTopic.Article, newItem: NytTopic.Article): Boolean {
            return oldItem.title == newItem.title
        }

    }
}