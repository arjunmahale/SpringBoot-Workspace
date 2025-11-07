/* Lightweight UI helpers for CollegeERP
   Place into: src/main/webapp/assets/js/ui.js
   Or: src/main/resources/static/assets/js/ui.js
*/
(function(){
  // DOM helpers
  const $ = selector => document.querySelector(selector);
  const $$ = selector => Array.from(document.querySelectorAll(selector));

  // Theme (dark/light) persisted
  const THEME_KEY = 'erp-theme';
  function applyTheme(theme){
    document.documentElement.dataset.theme = theme;
    if(theme === 'light'){
      document.documentElement.style.background = '';
      // Optionally toggle classes or other light-mode specifics
    }
  }
  function initTheme(){
    const saved = localStorage.getItem(THEME_KEY);
    if(saved){
      applyTheme(saved);
    } else {
      const prefersLight = window.matchMedia && window.matchMedia('(prefers-color-scheme: light)').matches;
      applyTheme(prefersLight ? 'light' : 'dark');
    }
    const btn = $('#theme-toggle');
    if(btn){
      btn.addEventListener('click', ()=>{
        const current = document.documentElement.dataset.theme || 'dark';
        const next = current === 'dark' ? 'light' : 'dark';
        applyTheme(next);
        localStorage.setItem(THEME_KEY, next);
        btn.setAttribute('aria-pressed', next === 'dark' ? 'false' : 'true');
      });
    }
  }

  // Ripple effect for buttons (delegated)
  function initRipples(){
    document.addEventListener('click', function(e){
      const el = e.target.closest('.btn');
      if(!el) return;
      const rect = el.getBoundingClientRect();
      const ripple = document.createElement('span');
      ripple.className = 'ripple';
      const size = Math.max(rect.width, rect.height) * 1.2;
      ripple.style.width = ripple.style.height = size + 'px';
      ripple.style.left = (e.clientX - rect.left - size/2) + 'px';
      ripple.style.top = (e.clientY - rect.top - size/2) + 'px';
      el.appendChild(ripple);
      setTimeout(()=> ripple.remove(), 700);
    }, true);
  }

  // Scroll reveal using IntersectionObserver
  function initReveal(){
    const items = $$('.reveal');
    if(!('IntersectionObserver' in window) || items.length === 0) {
      // fallback: reveal all
      items.forEach(i => i.classList.add('in'));
      return;
    }
    const obs = new IntersectionObserver((entries)=>{
      entries.forEach(e=>{
        if(e.isIntersecting){
          e.target.classList.add('in');
          obs.unobserve(e.target);
        }
      });
    }, {threshold: 0.08});
    items.forEach(i => obs.observe(i));
  }

  // Floating labels
  function initFloatingLabels(){
    const groups = $$('.form-group');
    groups.forEach(g=>{
      const input = g.querySelector('input,textarea,select');
      if(!input) return;
      const check = ()=> {
        if(input.value && input.value.trim() !== '') g.classList.add('has-value');
        else g.classList.remove('has-value');
      };
      input.addEventListener('input', check);
      input.addEventListener('change', check);
      input.addEventListener('focus', ()=> g.classList.add('focused'));
      input.addEventListener('blur', ()=> g.classList.remove('focused'));
      // initialize
      check();
    });
  }

  // Simple form validation (client-side)
  function initFormValidation(){
    const forms = document.querySelectorAll('form[data-validate]');
    forms.forEach(form=>{
      form.addEventListener('submit', function(e){
        let valid = true;
        const required = form.querySelectorAll('[data-required]');
        required.forEach(el=>{
          if(!el.value || (typeof el.value === 'string' && el.value.trim() === '')){
            valid = false;
            el.classList.add('invalid');
            if(!el.nextElementSibling || !el.nextElementSibling.classList.contains('error')) {
              const err = document.createElement('div');
              err.className = 'error';
              err.style.color = '#ffb4b4';
              err.style.marginTop = '6px';
              err.textContent = el.getAttribute('data-required') || 'This field is required';
              el.parentNode.appendChild(err);
            }
          } else {
            el.classList.remove('invalid');
            const nxt = el.parentNode.querySelector('.error');
            if(nxt) nxt.remove();
          }
        });
        if(!valid){
          e.preventDefault();
          form.querySelector('[data-submit-focus]')?.focus();
        }
      });
    });
  }

  // Initialize on DOM ready
  document.addEventListener('DOMContentLoaded', function(){
    initTheme();
    initRipples();
    initReveal();
    initFloatingLabels();
    initFormValidation();

    // tiny: demo search filter for tables
    const search = document.getElementById('table-search');
    if(search){
      search.addEventListener('input', function(){
        const q = this.value.toLowerCase();
        const rows = document.querySelectorAll('#students-table tbody tr');
        rows.forEach(r=>{
          const txt = r.textContent.toLowerCase();
          r.style.display = txt.includes(q) ? '' : 'none';
        });
      });
    }
  });

  // expose for debugging
  window.CollegeERPUI = {
    applyTheme
  };
})();