package com.veganafro.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.veganafro.model.NytTopic
import kotlinx.android.synthetic.main.nyt_trending_card.view.nyt_thumbnail
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

    override fun onBindViewHolder(holder: NytArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @Suppress("ReplaceGetOrSet")
    class NytArticleViewHolder(
        view: View,
        private val onArticleClickedCallback: (article: NytTopic.Article) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val titleView: TextView = view.nyt_title_text
        private val sectionView: TextView = view.nyt_section_text
        private val thumbnailView: ImageView = view.nyt_thumbnail

        fun bind(article: NytTopic.Article) {
            titleView.text = article.title
            sectionView.text = article.section
            thumbnailView.load(article.media.get(0).photos.get(
                article.media.get(0).photos.lastIndex
            ).url) {
                crossfade(true)
                placeholder(R.drawable.nyt_logo_dark)
            }

            itemView.setOnClickListener {
                onArticleClickedCallback.invoke(article)
            }
        }
    }

    class NytArticleDiffCallback : DiffUtil.ItemCallback<NytTopic.Article>() {
        override fun areItemsTheSame(oldItem: NytTopic.Article, newItem: NytTopic.Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NytTopic.Article, newItem: NytTopic.Article): Boolean {
            return oldItem.title == newItem.title && oldItem.url == newItem.url && oldItem.section == newItem.section
        }

    }
}
