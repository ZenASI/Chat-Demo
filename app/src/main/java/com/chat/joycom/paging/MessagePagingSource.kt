package com.chat.joycom.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.InvalidationTracker
import com.chat.joycom.model.Message
import com.chat.joycom.utils.RoomUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MessagePagingSource(val selfId: Long = 0L, val id: Long, val roomUtils: RoomUtils) :
    PagingSource<Int, Message>() {

    init {
        roomUtils.db.invalidationTracker.addObserver(object :
            InvalidationTracker.Observer(arrayOf(Message.TABLE_NAME)) {
            override fun onInvalidated(tables: Set<String>) {
                Timber.d("onInvalidated")
                this@MessagePagingSource.invalidate()
            }
        })
    }

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        val pos = state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.minus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(1)
        }
        Timber.d("getRefreshKey => ${pos}, anchorPosition => ${state.anchorPosition}")
        return pos
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        val position = params.key ?: 0
        val prevKey = (position * params.loadSize)
        val nextKey = (position + 1) * params.loadSize
        Timber.d("load key => ${position}, pre => ${prevKey}, next => ${nextKey}, ${this@MessagePagingSource}")
        return try {
            val result = withContext(Dispatchers.IO) {
                if (selfId == 0L) {
                    roomUtils.pagingMessageByGroupId(id, prevKey, nextKey)
                } else {
                    roomUtils.pagingMessageByUserId(selfId, id, prevKey, nextKey)
                }
            }
            result.zipWithNext { first, second ->
                // 確認相鄰的item是否顯示icon
                val firstUserId = first.fromUserId
                val secondUserId = second.fromUserId
                first.showIcon = firstUserId != secondUserId
            }
            Timber.d("load data => ${result.size}")
            LoadResult.Page(
                data = result,
                prevKey = if (position <= 0) null else position - 1,
                nextKey = if (result.isEmpty()) null else position + 1
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}