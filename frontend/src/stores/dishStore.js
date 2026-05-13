import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { dishApi, tagApi } from '@/api/dish'

export const useDishStore = defineStore('dish', () => {
  const dishes = ref([])
  const allTags = ref([])
  const loading = ref(false)
  const error = ref(null)

  // Filter / sort state
  const selectedTags = ref([])       // tag names — non-exclusive
  const sortBy = ref('ORDER_COUNT')  // ORDER_COUNT | CREATED_DATE
  const timePeriod = ref('LAST_MONTH')

  const filteredDishes = computed(() => {
    if (!selectedTags.value.length) return dishes.value
    return dishes.value.filter(d =>
      selectedTags.value.every(tag => d.tags.some(t => t.name === tag))
    )
  })

  async function fetchDishes() {
    loading.value = true
    error.value = null
    try {
      dishes.value = await dishApi.list(sortBy.value, timePeriod.value)
    } catch (e) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  async function fetchTags() {
    allTags.value = await tagApi.list()
  }

  async function createDish(payload) {
    const dish = await dishApi.create(payload.data)
    if (payload.photo) await dishApi.uploadPhoto(dish.id, payload.photo)
    await fetchDishes()
    return dish
  }

  async function updateDish(id, payload) {
    const dish = await dishApi.update(id, payload.data)
    if (payload.photo) await dishApi.uploadPhoto(id, payload.photo)
    else if (payload.removePhoto) await dishApi.deletePhoto(id)
    await fetchDishes()
    return dish
  }

  async function deleteDish(id) {
    await dishApi.remove(id)
    dishes.value = dishes.value.filter(d => d.id !== id)
  }

  function toggleTag(tagName) {
    const idx = selectedTags.value.indexOf(tagName)
    if (idx === -1) selectedTags.value.push(tagName)
    else selectedTags.value.splice(idx, 1)
  }

  return {
    dishes, allTags, loading, error,
    selectedTags, sortBy, timePeriod,
    filteredDishes,
    fetchDishes, fetchTags,
    createDish, updateDish, deleteDish,
    toggleTag
  }
})
