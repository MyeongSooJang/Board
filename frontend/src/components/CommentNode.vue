<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  comment: {
    type: Object,
    required: true
  },
  replyingToCommentId: Number,
  editingCommentId: Number,
  editCommentContent: String,
  isEditingComment: Boolean,
  currentUsername: String,
  isSubmittingReply: Boolean,
  replyContent: String,
  depth: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits([
  'reply-click',
  'reply-cancel',
  'reply-add',
  'edit-click',
  'edit-cancel',
  'edit-save',
  'delete'
])

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  return `${month}.${day} ${hours}:${minutes}:${seconds}`
}
</script>

<template>
  <!-- 댓글/대댓글 본체 -->
  <div :class="['comment-item', depth > 0 && 'is-nested']" :style="{ marginLeft: depth > 0 ? (depth * 24) + 'px' : '0' }">
      <div class="comment-header">
        <span class="comment-author">{{ comment.memberName }}</span>
        <span class="comment-date">{{ formatDate(comment.createTime) }}</span>
      </div>
      <div class="comment-content">{{ comment.content }}</div>
      <div class="comment-actions">
        <button
          @click="emit('reply-click', comment.commentId)"
          class="btn-small btn-reply"
        >
          답글
        </button>
        <button
          v-if="currentUsername && currentUsername == comment.memberName"
          @click="emit('edit-click', comment)"
          class="btn-small btn-edit"
        >
          수정
        </button>
        <button
          v-if="currentUsername && currentUsername == comment.memberName"
          @click="emit('delete', comment.commentId)"
          class="btn-small btn-danger"
        >
          삭제
        </button>
      </div>

      <!-- 댓글 수정 폼 -->
      <div v-if="editingCommentId === comment.commentId && isEditingComment" class="edit-form">
        <div class="edit-form-header">
          <span class="edit-form-title">댓글 수정</span>
          <button @click="emit('edit-cancel')" class="btn-close">✕</button>
        </div>
        <textarea
          :value="editCommentContent"
          @input="$emit('update:editCommentContent', $event.target.value)"
          placeholder="댓글을 수정하세요"
          class="edit-input"
          rows="3"
        ></textarea>
        <button
          @click="emit('edit-save', comment.commentId)"
          class="btn btn-comment-submit"
        >
          저장
        </button>
      </div>
    </div>

    <!-- 대댓글/대대댓글 입력 폼 -->
    <div v-if="replyingToCommentId === comment.commentId" :class="['reply-form', depth > 0 && 'is-nested']" :style="{ marginLeft: depth > 0 ? (depth * 24) + 'px' : '0' }">
      <div class="reply-form-header">
        <span class="reply-form-title">답글 작성</span>
        <button @click="emit('reply-cancel')" class="btn-close">✕</button>
      </div>
      <textarea
        :value="replyContent"
        @input="$emit('update:replyContent', $event.target.value)"
        placeholder="답글을 입력하세요"
        class="reply-input"
        rows="2"
      ></textarea>
      <button
        @click="emit('reply-add', comment.commentId)"
        class="btn btn-comment-submit"
        :disabled="isSubmittingReply"
      >
        {{ isSubmittingReply ? '작성 중' : '작성' }}
      </button>
    </div>

    <!-- 자식 댓글 (재귀) -->
    <template v-if="comment.children && comment.children.length > 0">
      <CommentNode
        v-for="child in comment.children"
        :key="child.commentId"
        :comment="child"
        :replyingToCommentId="replyingToCommentId"
        :editingCommentId="editingCommentId"
        :editCommentContent="editCommentContent"
        :isEditingComment="isEditingComment"
        :currentUsername="currentUsername"
        :isSubmittingReply="isSubmittingReply"
        :replyContent="replyContent"
        :depth="depth + 1"
        @reply-click="emit('reply-click', $event)"
        @reply-cancel="emit('reply-cancel')"
        @reply-add="emit('reply-add', $event)"
        @edit-click="emit('edit-click', $event)"
        @edit-cancel="emit('edit-cancel')"
        @edit-save="emit('edit-save', $event)"
        @delete="emit('delete', $event)"
      />
    </template>
</template>

<style scoped>
.comment-item {
  background: white;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  padding: 10px 12px;
  font-size: 13px;
  word-wrap: break-word;
  overflow-wrap: break-word;
  margin-bottom: 6px;
}

.comment-item.is-nested {
  background: #fafbfc;
  margin-left: 24px;
  border-left: 3px solid #3498db;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
  gap: 8px;
}

.comment-author {
  color: #3d414d;
  font-weight: 600;
  font-size: 12px;
  min-width: 0;
  word-break: break-all;
}

.comment-date {
  color: #999;
  font-size: 11px;
  white-space: nowrap;
  flex-shrink: 0;
}

.comment-content {
  padding: 4px 0;
  line-height: 1.6;
  white-space: pre-wrap;
  color: #333;
  font-size: 13px;
  word-break: break-word;
  text-align: left;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.comment-actions {
  margin-top: 6px;
  padding-top: 6px;
  border-top: 1px solid #e9ecef;
  text-align: left;
  display: flex;
  gap: 6px;
  align-items: center;
}

.btn-small {
  padding: 3px 8px;
  font-size: 11px;
  border: none;
  border-radius: 2px;
  cursor: pointer;
  transition: background 0.2s;
  background: #e9ecef;
  color: #666;
  font-weight: 500;
  display: inline-block;
}

.btn-small:hover {
  background: #ddd;
}

.btn-small.btn-danger {
  background: #e74c3c;
  color: white;
}

.btn-small.btn-danger:hover {
  background: #c0392b;
}

.btn-small.btn-reply {
  background: #3498db;
  color: white;
}

.btn-small.btn-reply:hover {
  background: #2980b9;
}

.btn-small.btn-edit {
  background: #f39c12;
  color: white;
}

.btn-small.btn-edit:hover {
  background: #e67e22;
}

.btn-close {
  background: none;
  border: none;
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.btn-close:hover {
  color: #333;
}

.reply-form {
  margin-bottom: 8px;
  background: #f9f9f9;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  padding: 10px 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.reply-form.is-nested {
  margin-left: 24px;
}

.edit-form {
  margin-top: 8px;
  margin-bottom: 8px;
  background: #f9f9f9;
  border: 1px solid #ffc107;
  border-radius: 3px;
  padding: 10px 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.reply-form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #666;
  font-weight: 500;
}

.reply-form-title {
  color: #3d414d;
}

.edit-form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #666;
  font-weight: 500;
}

.edit-form-title {
  color: #f39c12;
  font-weight: 600;
}

.reply-input {
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-family: inherit;
  font-size: 13px;
  resize: none;
  line-height: 1.4;
  box-sizing: border-box;
  min-height: 45px;
}

.reply-input:focus {
  outline: none;
  border-color: #3d414d;
}

.edit-input {
  padding: 8px 10px;
  border: 1px solid #ffc107;
  border-radius: 3px;
  font-family: inherit;
  font-size: 13px;
  resize: none;
  line-height: 1.4;
  box-sizing: border-box;
  min-height: 60px;
}

.edit-input:focus {
  outline: none;
  border-color: #f39c12;
  box-shadow: 0 0 0 3px rgba(243, 156, 18, 0.1);
}

.btn {
  padding: 6px 12px !important;
  font-size: 13px !important;
  font-weight: 500;
  background: #3d414d;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  transition: background 0.2s;
  white-space: nowrap;
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.btn:hover:not(:disabled) {
  background: #2c2f38;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-comment-submit {
  padding: 6px 12px !important;
  font-size: 13px !important;
  font-weight: 500;
  background: #3d414d;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  transition: background 0.2s;
  white-space: nowrap;
  flex-shrink: 0;
}

.btn-comment-submit:hover:not(:disabled) {
  background: #2c2f38;
}

.btn-comment-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .comment-item.is-nested {
    margin-left: 16px;
  }

  .reply-form.is-nested {
    margin-left: 16px;
  }
}
</style>
