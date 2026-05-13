import axios from 'axios'

const http = axios.create({ baseURL: '/api' })

export const dishApi = {
  list(sortBy = 'ORDER_COUNT', timePeriod = 'LAST_MONTH') {
    return http.get('/dishes', { params: { sortBy, timePeriod } }).then(r => r.data)
  },

  create(data) {
    return http.post('/dishes', data).then(r => r.data)
  },

  update(id, data) {
    return http.put(`/dishes/${id}`, data).then(r => r.data)
  },

  remove(id) {
    return http.delete(`/dishes/${id}`)
  },

  uploadPhoto(id, file) {
    const form = new FormData()
    form.append('file', file)
    return http.post(`/dishes/${id}/photo`, form)
  },

  deletePhoto(id) {
    return http.delete(`/dishes/${id}/photo`)
  },

  photoUrl(id) {
    return `/api/dishes/${id}/photo`
  }
}

export const tagApi = {
  list() {
    return http.get('/tags').then(r => r.data)
  }
}
