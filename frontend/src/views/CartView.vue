<template>
  <div class="page">
    <div class="container">
      <h1>Your cart</h1>

      <p v-if="loading" class="status">Loading…</p>
      <p v-else-if="cartStore.error" class="status error">Could not load cart. Please try again.</p>

      <div v-else-if="!cartStore.items.length" class="empty">
        <div class="empty-icon">🛒</div>
        <h2>Your cart is empty</h2>
        <p>Looks like you haven't added any dishes yet.</p>
        <RouterLink to="/menu" class="btn-primary">Browse menu</RouterLink>
      </div>

      <div v-else-if="cartStore.items.length" class="layout">
        <ul class="item-list">
          <li v-for="item in cartStore.items" :key="item.id" class="item">
            <div class="item-img-wrap">
              <img v-if="item.dish.hasPhoto" :src="`/api/dishes/${item.dish.id}/photo`" :alt="item.dish.name" />
              <div v-else class="item-img-placeholder">🍽️</div>
            </div>
            <div class="item-info">
              <span class="item-name">{{ item.dish.name }}</span>
              <div class="item-tags">
                <span v-for="tag in item.dish.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
              </div>
              <span class="item-meta">Qty: {{ item.quantity }}</span>
            </div>
            <button class="btn-remove" @click="cartStore.removeItem(item.id)" title="Remove">✕</button>
          </li>
        </ul>

        <aside class="summary">
          <h2>Order summary</h2>
          <div class="summary-row">
            <span>Items ({{ cartStore.itemCount }})</span>
            <span>—</span>
          </div>
          <div class="summary-row">
            <span>Service fee</span>
            <span>$2.00</span>
          </div>
          <div class="summary-divider"></div>
          <div class="summary-row total">
            <span>Total</span>
            <span>$2.00</span>
          </div>
          <p class="note">Dish prices are set by the restaurant</p>
          <button class="btn-checkout">Reserve &amp; pay</button>
          <p class="disclaimer">You won't be charged yet</p>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { useCartStore } from '@/stores/cartStore'

const cartStore = useCartStore()
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { await cartStore.fetchCart() } finally { loading.value = false }
})
</script>

<style scoped>
.page { padding: 2.5rem 0 5rem; }
.container { max-width: 980px; margin: 0 auto; padding: 0 2rem; }
h1 { font-size: 2rem; font-weight: 800; margin-bottom: 2rem; }

.status { color: var(--text-secondary); }
.status.error { color: #dc2626; }

/* Empty state */
.empty { text-align: center; padding: 5rem 0; display: flex; flex-direction: column; align-items: center; gap: 0.75rem; }
.empty-icon { font-size: 3.5rem; }
.empty h2   { font-size: 1.5rem; font-weight: 700; }
.empty p    { color: var(--text-secondary); }
.btn-primary {
  margin-top: 0.5rem;
  display: inline-block;
  padding: 0.75rem 1.75rem;
  background: var(--primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-btn);
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-primary:hover { background: var(--primary-hover); }

/* Layout */
.layout {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 2.5rem;
  align-items: start;
}
@media (max-width: 768px) { .layout { grid-template-columns: 1fr; } }

/* Item list */
.item-list { list-style: none; }
.item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem 0;
  border-bottom: 1px solid var(--border);
}

.item-img-wrap { width: 80px; height: 80px; flex-shrink: 0; border-radius: 10px; overflow: hidden; background: #f0f0f0; }
.item-img-wrap img { width: 100%; height: 100%; object-fit: cover; }
.item-img-placeholder { height: 100%; display: flex; align-items: center; justify-content: center; font-size: 1.75rem; }

.item-info { flex: 1; display: flex; flex-direction: column; gap: 0.25rem; }
.item-name { font-weight: 600; font-size: 1rem; }
.item-tags { display: flex; flex-wrap: wrap; gap: 4px; }
.tag { font-size: 0.72rem; padding: 2px 7px; border-radius: 999px;
  background: #fff0f0; color: var(--primary); border: 1px solid #ffc0cc; }
.item-meta { color: var(--text-secondary); font-size: 0.875rem; }

.btn-remove {
  padding: 0.35rem 0.6rem;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 6px;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 0.8rem;
  transition: background 0.15s, color 0.15s;
}
.btn-remove:hover { background: #fee2e2; color: #dc2626; border-color: #fca5a5; }

/* Summary */
.summary {
  background: var(--card-bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-card);
  padding: 1.5rem;
  position: sticky;
  top: 80px;
}
.summary h2 { font-size: 1.2rem; font-weight: 700; margin-bottom: 1.25rem; }

.summary-row {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  padding: 0.45rem 0;
  color: var(--text-secondary);
}
.summary-row.total { color: var(--text); font-weight: 700; font-size: 1rem; }
.summary-divider { border-top: 1px solid var(--border); margin: 0.5rem 0; }
.note { font-size: 0.78rem; color: var(--text-secondary); margin: 0.5rem 0 0; font-style: italic; }

.btn-checkout {
  width: 100%;
  margin-top: 1.25rem;
  padding: 0.9rem;
  background: var(--primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-btn);
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-checkout:hover { background: var(--primary-hover); }
.disclaimer { text-align: center; color: var(--text-secondary); font-size: 0.78rem; margin-top: 0.75rem; }
</style>
