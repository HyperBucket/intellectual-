import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartApi } from '@/api/cart'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])
  const error = ref(null)

  const total = computed(() =>
    items.value.reduce((sum, item) => sum + Number(item.dish.price ?? 0) * item.quantity, 0)
  )

  const itemCount = computed(() =>
    items.value.reduce((sum, item) => sum + item.quantity, 0)
  )

  async function fetchCart() {
    error.value = null
    try {
      items.value = await cartApi.list()
    } catch (e) {
      error.value = e.message
      items.value = []
    }
  }

  async function addItem(dishId) {
    try {
      await cartApi.add(dishId)
      await fetchCart()
    } catch (e) {
      error.value = e.message
    }
  }

  async function removeItem(itemId) {
    try {
      await cartApi.remove(itemId)
      items.value = items.value.filter(i => i.id !== itemId)
    } catch (e) {
      error.value = e.message
    }
  }

  return { items, error, total, itemCount, fetchCart, addItem, removeItem }
})
