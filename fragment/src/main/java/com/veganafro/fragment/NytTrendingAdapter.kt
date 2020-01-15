package com.veganafro.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        view: View,
        private val onArticleClickedCallback: (article: NytTopic.Article) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val titleView: TextView = view.nyt_title_text
        private val sectionView: TextView = view.nyt_section_text

        fun bind(article: NytTopic.Article) {
            titleView.text = article.title
            sectionView.text = article.section

            titleView.setOnClickListener {
                onArticleClickedCallback.invoke(article)
            }
            sectionView.setOnClickListener {
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
