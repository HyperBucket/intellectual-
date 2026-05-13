<template>
  <div class="dish-card">
    <div class="photo-wrapper">
      <img v-if="dish.hasPhoto" :src="photoUrl" :alt="dish.name" class="photo" loading="lazy" />
      <div v-else class="photo-placeholder">🍽️</div>
    </div>

    <div class="body">
      <div class="top-row">
        <h3 class="name">{{ dish.name }}</h3>
        <span v-if="dish.orderCount" class="order-count">{{ dish.orderCount }} orders</span>
      </div>

      <p v-if="dish.description" class="desc">{{ dish.description }}</p>

      <div class="tags">
        <span v-for="tag in dish.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
      </div>

      <div class="actions">
        <button class="btn-cart" @click="onAddToCart" :disabled="adding">
          {{ adding ? 'Adding…' : 'Add to cart' }}
        </button>
        <div class="edit-actions">
          <button class="btn-edit"   @click="$emit('edit', dish)">Edit</button>
          <button class="btn-delete" @click="$emit('delete', dish)">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { dishApi } from '@/api/dish'
import { useCartStore } from '@/stores/cartStore'

const props = defineProps({ dish: { type: Object, required: true } })
defineEmits(['edit', 'delete'])

const cartStore = useCartStore()
const adding = ref(false)

const photoUrl = computed(() => dishApi.photoUrl(props.dish.id))

async function onAddToCart() {
  adding.value = true
  try { await cartStore.addItem(props.dish.id) } finally { adding.value = false }
}
</script>

<style scoped>
.dish-card {
  border-radius: var(--radius-card);
  overflow: hidden;
  box-shadow: var(--card-shadow);
  background: var(--card-bg);
  display: flex;
  flex-direction: column;
  transition: transform 0.2s, box-shadow 0.2s;
}
.dish-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.15); }

.photo-wrapper { width: 100%; height: 190px; background: #f5f5f5; overflow: hidden; }
.photo { width: 100%; height: 100%; object-fit: cover; display: block; }
.photo-placeholder {
  width: 100%; height: 100%;
  display: flex; align-items: center; justify-content: center;
  font-size: 3rem; color: #ccc;
}

.body { padding: 1rem 1.1rem 1.1rem; display: flex; flex-direction: column; gap: 0.5rem; flex: 1; }

.top-row { display: flex; justify-content: space-between; align-items: baseline; gap: 0.5rem; }
.name { margin: 0; font-size: 1rem; font-weight: 700; }
.order-count { font-size: 0.75rem; color: var(--primary); font-weight: 600; white-space: nowrap; }

.desc {
  margin: 0; font-size: 0.85rem; color: var(--text-secondary); line-height: 1.4;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}

.tags { display: flex; flex-wrap: wrap; gap: 4px; }
.tag { font-size: 0.72rem; padding: 2px 8px; border-radius: 999px;
  background: #fff0f0; color: var(--primary); border: 1px solid #ffc0cc; }

.actions { margin-top: auto; display: flex; flex-direction: column; gap: 0.5rem; }

.btn-cart {
  width: 100%;
  padding: 0.6rem;
  background: var(--primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-btn);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-cart:hover:not(:disabled) { background: var(--primary-hover); }
.btn-cart:disabled { opacity: 0.6; cursor: not-allowed; }

.edit-actions { display: flex; gap: 0.5rem; }
.btn-edit, .btn-delete {
  flex: 1; padding: 0.4rem; border-radius: 6px; cursor: pointer;
  border: none; font-size: 0.82rem; font-weight: 600; transition: opacity 0.15s;
}
.btn-edit   { background: #f0fdf4; color: #16a34a; }
.btn-delete { background: #fef2f2; color: #dc2626; }
.btn-edit:hover, .btn-delete:hover { opacity: 0.8; }
</style>
