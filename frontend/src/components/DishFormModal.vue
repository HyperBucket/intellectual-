<template>
  <div class="overlay" @click.self="$emit('close')">
    <div class="modal">
      <h2>{{ isEdit ? 'Edit Dish' : 'Add Dish' }}</h2>

      <form @submit.prevent="submit">
        <!-- Name -->
        <div class="field">
          <label>Name *</label>
          <input v-model="form.name" required placeholder="Dish name" />
        </div>

        <!-- Description -->
        <div class="field">
          <label>Description</label>
          <textarea v-model="form.description" rows="3" placeholder="Brief description…" />
        </div>

        <!-- Tags -->
        <div class="field">
          <label>Tags</label>
          <div class="tag-input-row">
            <input
              v-model="tagInput"
              placeholder="Type tag and press Enter"
              @keydown.enter.prevent="addTag"
            />
            <button type="button" class="btn-add-tag" @click="addTag">Add</button>
          </div>
          <div class="tag-list">
            <span v-for="t in form.tags" :key="t" class="tag-chip">
              {{ t }}
              <button type="button" @click="removeTag(t)">✕</button>
            </span>
            <!-- Existing tags quick-pick -->
            <span
              v-for="tag in availableTags"
              :key="'pick-' + tag.id"
              class="tag-chip suggestion"
              @click="addExisting(tag.name)"
            >+ {{ tag.name }}</span>
          </div>
        </div>

        <!-- Photo -->
        <div class="field">
          <label>Photo</label>
          <div class="photo-area">
            <img v-if="previewUrl" :src="previewUrl" class="preview" alt="preview" />
            <div v-else-if="isEdit && dish.hasPhoto" class="current-photo-hint">
              Current photo kept — upload new to replace
            </div>
          </div>
          <input type="file" accept="image/*" @change="onFileChange" />
          <label v-if="isEdit && dish.hasPhoto && !selectedFile" class="remove-photo">
            <input type="checkbox" v-model="removePhoto" /> Remove existing photo
          </label>
        </div>

        <div class="actions">
          <button type="button" class="btn-cancel" @click="$emit('close')">Cancel</button>
          <button type="submit" class="btn-save" :disabled="saving">
            {{ saving ? 'Saving…' : 'Save' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useDishStore } from '@/stores/dishStore'

const props  = defineProps({ dish: { type: Object, default: null } })
const emit   = defineEmits(['close', 'saved'])
const store  = useDishStore()

const isEdit = computed(() => !!props.dish)
const saving = ref(false)

const form = ref({
  name:        props.dish?.name        ?? '',
  description: props.dish?.description ?? '',
  tags:        props.dish?.tags?.map(t => t.name) ?? []
})

const tagInput     = ref('')
const selectedFile = ref(null)
const previewUrl   = ref(null)
const removePhoto  = ref(false)

const availableTags = computed(() =>
  store.allTags.filter(t => !form.value.tags.includes(t.name))
)

function addTag() {
  const t = tagInput.value.trim()
  if (t && !form.value.tags.includes(t)) form.value.tags.push(t)
  tagInput.value = ''
}
function removeTag(t) { form.value.tags = form.value.tags.filter(x => x !== t) }
function addExisting(name) { if (!form.value.tags.includes(name)) form.value.tags.push(name) }

function onFileChange(e) {
  selectedFile.value = e.target.files[0] ?? null
  previewUrl.value   = selectedFile.value ? URL.createObjectURL(selectedFile.value) : null
}

async function submit() {
  saving.value = true
  try {
    const payload = {
      data:        { name: form.value.name, description: form.value.description, tags: form.value.tags },
      photo:       selectedFile.value,
      removePhoto: removePhoto.value
    }
    if (isEdit.value) await store.updateDish(props.dish.id, payload)
    else              await store.createDish(payload)
    emit('saved')
    emit('close')
  } finally {
    saving.value = false
  }
}

onMounted(() => store.fetchTags())
</script>

<style scoped>
.overlay {
  position: fixed; inset: 0; background: rgba(0,0,0,0.45);
  display: flex; align-items: center; justify-content: center; z-index: 100;
}
.modal {
  background: #fff; border-radius: 14px; padding: 28px; width: 480px;
  max-width: 95vw; max-height: 90vh; overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0,0,0,0.25);
}
h2 { margin: 0 0 20px; font-size: 1.2rem; }
.field { display: flex; flex-direction: column; gap: 6px; margin-bottom: 16px; }
label { font-size: 0.85rem; font-weight: 600; color: #444; }
input[type="text"], input:not([type="file"]):not([type="checkbox"]), textarea {
  padding: 8px 10px; border-radius: 7px; border: 1px solid #d0d0d0;
  font-size: 0.9rem; width: 100%; box-sizing: border-box;
}
textarea { resize: vertical; }
.tag-input-row { display: flex; gap: 6px; }
.btn-add-tag {
  padding: 6px 12px; border-radius: 6px; border: none;
  background: #f0f0f0; cursor: pointer; font-size: 0.85rem;
}
.tag-list { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 6px; }
.tag-chip {
  display: flex; align-items: center; gap: 4px;
  padding: 3px 10px; border-radius: 999px; font-size: 0.8rem;
  background: #fff7ed; color: #ea580c; border: 1px solid #fed7aa;
}
.tag-chip button { border: none; background: none; cursor: pointer; color: inherit; padding: 0; }
.tag-chip.suggestion {
  background: #f0fdf4; color: #16a34a; border-color: #bbf7d0; cursor: pointer;
}
.photo-area { margin-bottom: 6px; }
.preview { width: 100%; max-height: 160px; object-fit: cover; border-radius: 8px; }
.current-photo-hint { font-size: 0.8rem; color: #888; font-style: italic; }
.remove-photo { display: flex; align-items: center; gap: 6px; font-size: 0.82rem; color: #dc2626; cursor: pointer; }
.actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 8px; }
.btn-cancel { padding: 8px 18px; border-radius: 7px; border: 1px solid #d0d0d0; background: #fff; cursor: pointer; }
.btn-save {
  padding: 8px 22px; border-radius: 7px; border: none;
  background: var(--primary, #FF385C); color: #fff; font-weight: 700; cursor: pointer;
  transition: background 0.15s;
}
.btn-save:hover:not(:disabled) { background: var(--primary-hover, #E31C5F); }
.btn-save:disabled { opacity: 0.6; cursor: not-allowed; }
</style>
