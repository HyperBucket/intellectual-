import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartApi } from '@/api/cart'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  const total = computed(() =>
    items.value.reduce((sum, item) => sum + Number(item.dish.price ?? 0) * item.quantity, 0)
  )

  const itemCount = computed(() =>
    items.value.reduce((sum, item) => sum + item.quantity, 0)
  )

  async function fetchCart() {
    items.value = await cartApi.list()
  }

  async function addItem(dishId) {
    await cartApi.add(dishId)
    await fetchCart()
  }

  async function removeItem(itemId) {
    await cartApi.remove(itemId)
    items.value = items.value.filter(i => i.id !== itemId)
  }

  return { items, total, itemCount, fetchCart, addItem, removeItem }
})
