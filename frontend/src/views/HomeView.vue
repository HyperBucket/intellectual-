<template>
  <div>
    <!-- Hero -->
    <section class="hero">
      <div class="hero-content">
        <h1>Dishes worth<br>every bite</h1>
        <p>Handcrafted meals made with the finest ingredients.</p>
        <RouterLink to="/menu" class="btn-hero">Explore the menu</RouterLink>
      </div>
    </section>

    <!-- Featured -->
    <section class="featured">
      <div class="container">
        <h2>Featured right now</h2>
        <p class="sub">Our most loved plates this month</p>

        <p v-if="loading" class="status">Loading…</p>

        <div v-else-if="featured.length" class="grid">
          <div v-for="dish in featured" :key="dish.id" class="dish-card">
            <div class="img-wrap">
              <img v-if="dish.hasPhoto" :src="`/api/dishes/${dish.id}/photo`" :alt="dish.name" />
              <div v-else class="img-placeholder">🍽️</div>
            </div>
            <div class="card-body">
              <div class="card-top">
                <span class="dish-name">{{ dish.name }}</span>
                <span v-if="dish.orderCount" class="order-count">{{ dish.orderCount }} orders</span>
              </div>
              <p v-if="dish.description" class="dish-desc">{{ dish.description }}</p>
              <div class="tags">
                <span v-for="tag in dish.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
              </div>
              <button class="btn-cart" @click="addToCart(dish)" :disabled="adding[dish.id]">
                {{ adding[dish.id] ? 'Adding…' : 'Add to cart' }}
              </button>
            </div>
          </div>
        </div>

        <p v-else class="status">
          No dishes yet. <RouterLink to="/menu" class="link">Add the first one →</RouterLink>
        </p>

        <div class="see-all">
          <RouterLink to="/menu" class="btn-outline">See all dishes</RouterLink>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { RouterLink } from 'vue-router'
import { useDishStore } from '@/stores/dishStore'
import { useCartStore } from '@/stores/cartStore'

const dishStore = useDishStore()
const cartStore = useCartStore()

const loading = ref(false)
const adding  = reactive({})

const featured = computed(() => dishStore.dishes.slice(0, 3))

onMounted(async () => {
  loading.value = true
  try { await dishStore.fetchDishes() } finally { loading.value = false }
})

async function addToCart(dish) {
  adding[dish.id] = true
  try { await cartStore.addItem(dish.id) } finally { adding[dish.id] = false }
}
</script>

<style scoped>
/* Hero */
.hero {
  background: linear-gradient(135deg, #FF385C 0%, #C13584 100%);
  color: #fff;
  padding: 5rem 2rem 6rem;
  text-align: center;
}
.hero-content { max-width: 580px; margin: 0 auto; }
.hero h1 { font-size: clamp(2rem, 5vw, 3.25rem); font-weight: 800; line-height: 1.15; margin-bottom: 1rem; }
.hero p  { font-size: 1.1rem; opacity: 0.9; margin-bottom: 2rem; }
.btn-hero {
  display: inline-block;
  padding: 0.85rem 2rem;
  background: #fff;
  color: var(--primary);
  font-weight: 700;
  border-radius: var(--radius-btn);
  transition: opacity 0.15s;
}
.btn-hero:hover { opacity: 0.9; }

/* Featured section */
.featured { padding: 3rem 0 5rem; }
.container { max-width: 1100px; margin: 0 auto; padding: 0 2rem; }
h2  { font-size: 1.75rem; font-weight: 800; margin-bottom: 0.35rem; }
.sub { color: var(--text-secondary); margin-bottom: 2rem; }
.status { color: var(--text-secondary); text-align: center; padding: 3rem 0; }
.link { color: var(--primary); font-weight: 600; }

/* Grid */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.dish-card {
  background: var(--card-bg);
  border-radius: var(--radius-card);
  box-shadow: var(--card-shadow);
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}
.dish-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.15); }

.img-wrap  { height: 200px; overflow: hidden; background: #f0f0f0; }
.img-wrap img { width: 100%; height: 100%; object-fit: cover; }
.img-placeholder { height: 100%; display: flex; align-items: center; justify-content: center; font-size: 3rem; }

.card-body { padding: 1rem 1.25rem 1.25rem; }
.card-top  { display: flex; justify-content: space-between; align-items: baseline; gap: 0.5rem; margin-bottom: 0.35rem; }
.dish-name  { font-weight: 700; font-size: 1rem; }
.order-count { font-size: 0.78rem; color: var(--primary); font-weight: 600; white-space: nowrap; }
.dish-desc  { font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 0.6rem;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }

.tags { display: flex; flex-wrap: wrap; gap: 4px; margin-bottom: 0.9rem; }
.tag { font-size: 0.75rem; padding: 2px 8px; border-radius: 999px;
  background: #fff0f0; color: var(--primary); border: 1px solid #ffc0cc; }

.btn-cart {
  width: 100%;
  padding: 0.65rem;
  background: var(--primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-btn);
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-cart:hover:not(:disabled) { background: var(--primary-hover); }
.btn-cart:disabled { opacity: 0.6; cursor: not-allowed; }

/* See all */
.see-all { margin-top: 2.5rem; text-align: center; }
.btn-outline {
  display: inline-block;
  padding: 0.75rem 2rem;
  border: 2px solid var(--text);
  color: var(--text);
  font-weight: 600;
  border-radius: var(--radius-btn);
  transition: background 0.15s, color 0.15s;
}
.btn-outline:hover { background: var(--text); color: #fff; }
</style>
