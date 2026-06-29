// Shared API helper
const API = {
  base: '',
  token: () => localStorage.getItem('sems_token'),
  user:  () => ({ username: localStorage.getItem('sems_user'), role: localStorage.getItem('sems_role') }),
  setAuth(t, u, r){ localStorage.setItem('sems_token',t); localStorage.setItem('sems_user',u); localStorage.setItem('sems_role',r); },
  clear(){ localStorage.removeItem('sems_token'); localStorage.removeItem('sems_user'); localStorage.removeItem('sems_role'); },
  headers(json=true){
    const h = {};
    if (json) h['Content-Type']='application/json';
    const t = API.token();
    if (t) h['Authorization']='Bearer '+t;
    return h;
  },
  async req(method, url, body, isForm=false){
    const opts = { method, headers: API.headers(!isForm) };
    if (body) opts.body = isForm ? body : JSON.stringify(body);
    const r = await fetch(url, opts);
    if (r.status === 401 || r.status === 403){
      API.clear(); location.href='/login.html'; return;
    }
    if (!r.ok){
      let msg='Request failed';
      try { const j = await r.json(); msg = j.message || msg; } catch {}
      throw new Error(msg);
    }
    if (r.status === 204) return null;
    const ct = r.headers.get('content-type')||'';
    return ct.includes('json') ? r.json() : r.blob();
  },
  get:(u)=>API.req('GET',u),
  post:(u,b)=>API.req('POST',u,b),
  put:(u,b)=>API.req('PUT',u,b),
  patch:(u)=>API.req('PATCH',u),
  del:(u)=>API.req('DELETE',u),
};

function requireAuth(){
  if (!API.token()){ location.href='/login.html'; return false; }
  return true;
}
function logout(){ API.clear(); location.href='/login.html'; }

function renderShell(active){
  const u = API.user();
  const isAdmin = u.role === 'ADMIN';
  return `
  <div class="shell">
    <aside class="sidebar">
      <div class="logo">⚡ <span class="brand">SEMS</span></div>
      <a href="/dashboard.html" class="${active==='dashboard'?'active':''}">📊 Dashboard</a>
      <a href="/employees.html" class="${active==='employees'?'active':''}">👥 Employees</a>
      <a href="/departments.html" class="${active==='departments'?'active':''}">🏢 Departments</a>
      <a href="/leaves.html" class="${active==='leaves'?'active':''}">🌴 Leaves</a>
      <a href="/profile.html" class="${active==='profile'?'active':''}">👤 Profile</a>
      ${isAdmin ? `<a href="/reports.html" class="${active==='reports'?'active':''}">📑 Reports</a>` : ``}
      <div class="footer">v1.0 · ${u.role||''}</div>
    </aside>
    <main class="main" id="main"></main>
  </div>`;
}

function topbar(title){
  const u = API.user();
  return `<div class="topbar">
    <h2>${title}</h2>
    <div class="user-chip">
      <span>👋 ${u.username||''} <small class="text-muted">(${u.role||''})</small></span>
      <button class="btn btn-sm btn-outline-primary ms-2" onclick="logout()">Logout</button>
    </div>
  </div>`;
}

function badgeStatus(s){
  const k = (s||'').toLowerCase();
  const cls = ({active:'b-active',inactive:'b-inactive',pending:'b-pending',approved:'b-approved',rejected:'b-rejected'})[k]||'b-pending';
  return `<span class="badge-pill ${cls}">${s||''}</span>`;
}
