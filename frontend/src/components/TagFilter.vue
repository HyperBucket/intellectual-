<template>
  <div class="tag-filter">
    <span class="label">Filter by tag</span>
    <div class="tags">
      <button
        v-for="tag in store.allTags"
        :key="tag.id"
        class="tag-chip"
        :class="{ active: store.selectedTags.includes(tag.name) }"
        @click="store.toggleTag(tag.name)"
      >
        {{ tag.name }}
      </button>
      <button
        v-if="store.selectedTags.length"
        class="tag-chip clear"
        @click="store.selectedTags = []"
      >
        ✕ Clear
      </button>
    </div>
  </div>
</template>

<script setup>
import { useDishStore } from '@/stores/dishStore'
const store = useDishStore()
</script>

<style scoped>
.tag-filter {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}
.label {
  font-weight: 600;
  color: #555;
  white-space: nowrap;
}
.tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.tag-chip {
  padding: 4px 12px;
  border-radius: 999px;
  border: 1px solid #d0d0d0;
  background: #f5f5f5;
  cursor: pointer;
  font-size: 0.85rem;
  transition: all 0.15s;
}
.tag-chip:hover   { border-color: var(--primary, #FF385C); color: var(--primary, #FF385C); }
.tag-chip.active  { background: var(--primary, #FF385C); color: #fff; border-color: var(--primary, #FF385C); }
.tag-chip.clear   { background: #fee2e2; border-color: #fca5a5; color: #dc2626; }
</style>
