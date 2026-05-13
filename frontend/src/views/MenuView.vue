<template>
  <div class="menu-page">
    <!-- Header -->
    <div class="page-header">
      <h1>Menu</h1>
      <button class="btn-add" @click="openCreate">+ Add Dish</button>
    </div>

    <!-- Controls -->
    <div class="controls">
      <TagFilter />
      <SortBar />
    </div>

    <!-- Loading / Error -->
    <div v-if="store.loading" class="state-msg">Loading dishes…</div>
    <div v-else-if="store.error" class="state-msg error">{{ store.error }}</div>

    <!-- Grid -->
    <div v-else class="dish-grid">
      <DishCard
        v-for="dish in store.filteredDishes"
        :key="dish.id"
        :dish="dish"
        @edit="openEdit"
        @delete="confirmDelete"
      />
      <div v-if="!store.filteredDishes.length" class="empty">
        <span v-if="store.selectedTags.length">No dishes found for the selected filters.</span>
        <span v-else>No dishes found.</span>
      </div>
    </div>

    <!-- Modals -->
    <DishFormModal
      v-if="showModal"
      :dish="editingDish"
      @close="showModal = false"
      @saved="store.fetchDishes()"
    />

    <div v-if="deletingDish" class="overlay" @click.self="deletingDish = null">
      <div class="confirm-dialog">
        <p>Delete <strong>{{ deletingDish.name }}</strong>? This cannot be undone.</p>
        <div class="confirm-actions">
          <button @click="deletingDish = null">Cancel</button>
          <button class="btn-confirm-delete" @click="doDelete">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useDishStore } from '@/stores/dishStore'
import TagFilter from '@/components/TagFilter.vue'
import SortBar from '@/components/SortBar.vue'
import DishCard from '@/components/DishCard.vue'
import DishFormModal from '@/components/DishFormModal.vue'

const store = useDishStore()

const showModal   = ref(false)
const editingDish = ref(null)
const deletingDish = ref(null)

function openCreate() { editingDish.value = null; showModal.value = true }
function openEdit(dish) { editingDish.value = dish; showModal.value = true }
function confirmDelete(dish) { deletingDish.value = dish }

async function doDelete() {
  try {
    await store.deleteDish(deletingDish.value.id)
  } finally {
    deletingDish.value = null
  }
}

onMounted(async () => {
  await Promise.all([store.fetchDishes(), store.fetchTags()])
})
</script>

<style scoped>
.menu-page { max-width: 1100px; margin: 0 auto; padding: 2rem 1.5rem 4rem; }

.page-header {
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;
}
.page-header h1 { margin: 0; font-size: 2rem; font-weight: 800; }
.btn-add {
  padding: 0.6rem 1.2rem; border-radius: var(--radius-btn); border: none;
  background: var(--primary); color: #fff; font-weight: 700; cursor: pointer; font-size: 0.9rem;
  transition: background 0.15s;
}
.btn-add:hover { background: var(--primary-hover); }

.controls {
  display: flex; flex-wrap: wrap; gap: 1rem;
  justify-content: space-between; align-items: center;
  margin-bottom: 1.5rem; padding: 0.9rem 1rem;
  background: var(--card-bg); border-radius: var(--radius-card);
  box-shadow: 0 1px 4px rgba(0,0,0,0.07); border: 1px solid var(--border);
}

.dish-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 1.25rem;
}

.state-msg { padding: 40px; text-align: center; color: #888; }
.state-msg.error { color: #dc2626; }
.empty { grid-column: 1/-1; text-align: center; color: #aaa; padding: 40px; }

/* Delete confirm */
.overlay {
  position: fixed; inset: 0; background: rgba(0,0,0,0.4);
  display: flex; align-items: center; justify-content: center; z-index: 100;
}
.confirm-dialog {
  background: #fff; padding: 28px; border-radius: 12px;
  max-width: 380px; width: 90%; box-shadow: 0 8px 30px rgba(0,0,0,0.2);
}
.confirm-dialog p { margin: 0 0 20px; }
.confirm-actions { display: flex; gap: 10px; justify-content: flex-end; }
.confirm-actions button {
  padding: 8px 18px; border-radius: 7px; cursor: pointer;
  border: 1px solid #d0d0d0; background: #fff;
}
.btn-confirm-delete { background: #dc2626 !important; color: #fff; border-color: #dc2626 !important; }
</style>
