package com.chat.joycom.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.paging.util.INVALID
import androidx.room.paging.util.ThreadSafeInvalidationObserver
import com.chat.joycom.model.Message
import com.chat.joycom.utils.RoomUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MessagePagingSource(val selfId: Long = 0L, val id: Long, val roomUtils: RoomUtils) :
    PagingSource<Int, Message>() {

    private val observer = ThreadSafeInvalidationObserver(
        tables = arrayOf(Message.TABLE_NAME),
        onInvalidated = ::invalidate
    )

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        val prevKey = state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey
        }
        val nextKey = state.anchorPosition?.let {
            state.closestPageToPosition(it)?.nextKey
        }
        Timber.d("getRefreshKey prevKey => ${prevKey}, nextKey => ${nextKey}")
        return if (prevKey != null && nextKey == null) {
            // top
            prevKey + 1
        } else if (prevKey == null && nextKey != null) {
            // bottom
            maxOf(nextKey - 1, 0)
        } else if (prevKey != null && nextKey != null) {
            (prevKey + nextKey) / 2
        } else {
            null
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        return withContext(Dispatchers.IO) {
            // table observe
//            observer.registerIfNecessary(roomUtils.db)
            val key = params.key ?: 0
            val offset = if (key == 0) 0 else key * params.loadSize
            val limit = params.loadSize
            try {
                val result = if (selfId == 0L) {
                    listOf(
                        Message.getFakeMsg(), Message.getFakeMsg(), Message.getFakeMsg(),
                        Message.getFakeMsg(), Message.getFakeMsg(), Message.getFakeMsg()
                    )
//                    roomUtils.pagingMessageByGroupId(id, offset, limit)
                } else {
//                    roomUtils.pagingMessageByUserId(selfId, id, offset, limit)
                    listOf(
                        Message.getFakeMsg(), Message.getFakeMsg(), Message.getFakeMsg(),
                        Message.getFakeMsg(), Message.getFakeMsg(), Message.getFakeMsg()
                    )
                }
                result.zipWithNext { first, second ->
                    // 確認相鄰的item是否顯示icon
                    val firstUserId = first.fromUserId
                    val secondUserId = second.fromUserId
                    first.showIcon = firstUserId != secondUserId
                }

                Timber.d("load key => ${key}, offset => ${offset}, size => ${result.size}")

                roomUtils.db.invalidationTracker.refreshVersionsSync()
                @Suppress("UNCHECKED_CAST")
                if (invalid) {
                    INVALID as LoadResult.Invalid<Int, Message>
                } else {
                    LoadResult.Page(
                        data = result,
                        itemsBefore = offset,
                        itemsAfter = if (result.isEmpty() || result.size < params.loadSize) 0 else offset + params.loadSize,
                        prevKey = if (key <= 0 || result.isEmpty()) null else key - 1,
                        nextKey = if (result.isEmpty() || result.size < params.loadSize) null else key + 1
                    )
                }
            } catch (e: Throwable) {
                Timber.d("load error => ${e.message}")
                LoadResult.Error(e)
            }
        }
    }

    override val jumpingSupported: Boolean
        get() = true
}