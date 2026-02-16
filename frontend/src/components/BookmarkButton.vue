<script setup>
import { ref, onMounted } from 'vue'
import { bookmarkApi } from '../api/bookmark'

const props = defineProps({
  boardId: {
    type: Number,
    required: true
  },
  isLoggedIn: {
    type: Boolean,
    default: false
  }
})

const isBookmarked = ref(false)
const bookmarkCount = ref(0)
const isToggling = ref(false)

onMounted(async () => {
  if (props.isLoggedIn) {
    await fetchBookmarkStatus()
  }
})

const fetchBookmarkStatus = async () => {
  try {
    const response = await bookmarkApi.getStatus(props.boardId)
    isBookmarked.value = response.data.bookmarked
    bookmarkCount.value = response.data.bookmarkCount
  } catch (err) {
    console.error('북마크 상태 조회 실패:', err)
  }
}

const handleToggleBookmark = async () => {
  if (!props.isLoggedIn) {
    alert('북마크하려면 로그인해주세요')
    return
  }

  isToggling.value = true

  try {
    const response = await bookmarkApi.toggleBookmark(props.boardId)
    // 반환된 상태로 업데이트
    isBookmarked.value = response.data.bookmarked
    bookmarkCount.value = response.data.bookmarkCount
  } catch (err) {
    alert(err.response?.data?.message || '북마크 저장 실패')
    console.error('북마크 토글 실패:', err)
  } finally {
    isToggling.value = false
  }
}
</script>

<template>
  <button
    @click="handleToggleBookmark"
    class="btn-bookmark"
    :class="{ bookmarked: isBookmarked }"
    :disabled="isToggling || !isLoggedIn"
  >
    {{ isBookmarked ? '⭐' : '☆' }} {{ bookmarkCount }}
  </button>
</template>

<style scoped>
.btn-bookmark {
  background: none;
  border: 1px solid #ddd;
  padding: 4px 8px;
  border-radius: 12px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  color: #666;
  transition: all 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;
}

.btn-bookmark:hover:not(:disabled) {
  background: #fff9e0;
  border-color: #ffc107;
  color: #ffc107;
}

.btn-bookmark.bookmarked {
  background: #fff9e0;
  border-color: #ffc107;
  color: #ffc107;
}

.btn-bookmark:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
