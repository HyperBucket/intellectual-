<template>
  <div class="sort-bar">
    <div class="group">
      <label>Sort by</label>
      <select v-model="store.sortBy" @change="store.fetchDishes()">
        <option value="ORDER_COUNT">Order count</option>
        <option value="CREATED_DATE">Date added</option>
      </select>
    </div>

    <div class="group" v-if="store.sortBy === 'ORDER_COUNT'">
      <label>Period</label>
      <select v-model="store.timePeriod" @change="store.fetchDishes()">
        <option value="LAST_WEEK">Last week</option>
        <option value="LAST_MONTH">Last month</option>
        <option value="LAST_YEAR">Last year</option>
        <option value="ALL_TIME">All time</option>
      </select>
    </div>
  </div>
</template>

<script setup>
import { useDishStore } from '@/stores/dishStore'
const store = useDishStore()
</script>

<style scoped>
.sort-bar {
  display: flex;
  gap: 16px;
  align-items: center;
}
.group {
  display: flex;
  align-items: center;
  gap: 6px;
}
label {
  font-size: 0.85rem;
  color: #555;
}
select {
  padding: 4px 10px;
  border-radius: 6px;
  border: 1px solid #d0d0d0;
  background: #fff;
  cursor: pointer;
  font-size: 0.9rem;
}
</style>
