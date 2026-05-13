<template>
  <div id="app">
    <header class="navbar">
      <RouterLink to="/" class="brand">
        <svg class="logo-icon" viewBox="0 0 32 32" aria-hidden="true">
          <path d="M16 2C8.268 2 2 8.268 2 16s6.268 14 14 14 14-6.268 14-14S23.732 2 16 2zm0 5a4 4 0 110 8 4 4 0 010-8zm0 19.5c-3.5 0-6.6-1.8-8.5-4.5.044-2.8 5.667-4.35 8.5-4.35 2.822 0 8.456 1.55 8.5 4.35C22.6 24.7 19.5 26.5 16 26.5z"/>
        </svg>
        Intellectual
      </RouterLink>

      <nav class="nav-links">
        <RouterLink to="/menu">Menu</RouterLink>
        <RouterLink to="/cart" class="cart-link">
          <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
            <path d="M1 1h4l2.68 13.39a2 2 0 001.98 1.61h9.72a2 2 0 001.98-1.61L23 6H6"/>
          </svg>
          Cart
          <span v-if="cartStore.itemCount > 0" class="badge">{{ cartStore.itemCount }}</span>
        </RouterLink>
      </nav>
    </header>

    <main>
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { RouterView, RouterLink } from 'vue-router'
import { useCartStore } from '@/stores/cartStore'

const cartStore = useCartStore()
</script>

<style>
/* ── Design tokens ─────────────────────────────── */
:root {
  --primary: #FF385C;
  --primary-hover: #E31C5F;
  --text: #222222;
  --text-secondary: #717171;
  --border: #DDDDDD;
  --bg: #F7F7F7;
  --card-bg: #ffffff;
  --card-shadow: 0 2px 16px rgba(0,0,0,0.12);
  --radius-card: 12px;
  --radius-btn: 8px;
  --font: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* ── Reset ─────────────────────────────────────── */
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: var(--font); background: var(--bg); color: var(--text); line-height: 1.5; }
a { text-decoration: none; color: inherit; }

/* ── Navbar ────────────────────────────────────── */
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
  height: 64px;
  background: #fff;
  border-bottom: 1px solid var(--border);
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.2rem;
  font-weight: 800;
  color: var(--primary);
}
.logo-icon { width: 26px; height: 26px; fill: var(--primary); }

.nav-links {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}
.nav-links a {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  padding: 0.45rem 0.9rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
  color: var(--text-secondary);
  transition: background 0.15s, color 0.15s;
}
.nav-links a:hover          { background: var(--bg); color: var(--text); }
.nav-links a.router-link-active { background: var(--bg); color: var(--text); font-weight: 600; }

.cart-link { position: relative; }
.badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  background: var(--primary);
  color: #fff;
  border-radius: 9999px;
  font-size: 0.68rem;
  font-weight: 700;
  margin-left: 2px;
}

main { min-height: calc(100vh - 64px); }
</style>
