import axios from 'axios'

const http = axios.create({ baseURL: '/api' })

export const cartApi = {
  list()         { return http.get('/cart').then(r => r.data) },
  add(dishId)    { return http.post('/cart/items', { dishId }).then(r => r.data) },
  remove(itemId) { return http.delete(`/cart/items/${itemId}`) },
}
