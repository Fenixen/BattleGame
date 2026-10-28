// Small vanilla JS client for the BattleGame REST API.
const EVENT_DELAY_MS = 400;

const $ = id => document.getElementById(id);

async function api(path, options = {}) {
    const response = await fetch(path, {
        headers: {'Content-Type': 'application/json'},
        ...options
    });
    if (response.status === 204) {
        return null;
    }
    const body = await response.json();
    if (!response.ok) {
        const details = body.errors ? Object.values(body.errors).join('\n') : body.detail;
        throw new Error(details || response.statusText);
    }
    return body;
}

const escapeHtml = text => String(text ?? '').replace(/[&<>"']/g,
    c => ({'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'}[c]));

async function loadFighters() {
    const fighters = await api('/api/fighters');

    $('fighterTable').innerHTML = fighters.map(f => `
        <tr>
            <td>${escapeHtml(f.name)}</td>
            <td>${f.type}</td>
            <td>${f.health}</td>
            <td>${f.damage}</td>
            <td>${f.defense}</td>
            <td>${f.mana ?? '-'}</td>
            <td>${f.magicDamage ?? '-'}</td>
            <td><button class="link" data-delete="${f.id}">delete</button></td>
        </tr>`).join('');

    const options = fighters.map(f => `<option value="${f.id}">${escapeHtml(f.name)}</option>`).join('');
    for (const select of [$('fighterA'), $('fighterB')]) {
        const previous = select.value;
        select.innerHTML = options;
        if (previous) select.value = previous;
    }
    if (!$('fighterB').value || $('fighterA').value === $('fighterB').value) {
        $('fighterB').selectedIndex = Math.min(1, fighters.length - 1);
    }
}

async function loadHistory() {
    const battles = await api('/api/battles');
    $('history').innerHTML = battles.length === 0
        ? '<li class="muted">No battles yet</li>'
        : battles.map(b => `
            <li>${escapeHtml(b.fighterA)} vs ${escapeHtml(b.fighterB)} —
                <strong>${b.winner ? escapeHtml(b.winner) + ' won' : 'draw'}</strong>
                <span class="muted">(${b.rounds} rounds, ${new Date(b.foughtAt).toLocaleString()})</span>
            </li>`).join('');
}

function renderStatus(fighters) {
    $('statusBars').innerHTML = fighters.map(f => `
        <div>
            <div class="fighter-name">${escapeHtml(f.name)}</div>
            <div class="bar"><div class="health" style="width:${100 * f.health / f.maxHealth}%"></div></div>
            <div class="bar-label">HP ${f.health} / ${f.maxHealth}</div>
            ${f.maxMana ? `
                <div class="bar"><div class="mana" style="width:${100 * f.mana / f.maxMana}%"></div></div>
                <div class="bar-label">Mana ${f.mana} / ${f.maxMana}</div>` : ''}
        </div>`).join('');
}

const sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

async function fight() {
    const button = $('fightButton');
    const log = $('battleLog');
    button.disabled = true;
    log.innerHTML = '';
    try {
        const battle = await api('/api/battles', {
            method: 'POST',
            body: JSON.stringify({fighterAId: Number($('fighterA').value), fighterBId: Number($('fighterB').value)})
        });
        // replay the battle log step by step
        for (const [index, event] of battle.events.entries()) {
            renderStatus(event.fighters);
            const item = document.createElement('li');
            item.textContent = event.round > 0 ? `[round ${event.round}] ${event.message}` : event.message;
            if (index === battle.events.length - 1) item.className = 'final';
            log.appendChild(item);
            log.scrollTop = log.scrollHeight;
            await sleep(EVENT_DELAY_MS);
        }
        await loadHistory();
    } catch (error) {
        log.innerHTML = `<li class="final">${escapeHtml(error.message)}</li>`;
    } finally {
        button.disabled = false;
    }
}

async function createFighter(event) {
    event.preventDefault();
    const form = event.target;
    const data = Object.fromEntries(new FormData(form));
    const toNumber = value => value === '' ? null : Number(value);
    const body = {
        name: data.name,
        type: data.type,
        health: toNumber(data.health),
        damage: toNumber(data.damage),
        defense: toNumber(data.defense),
        mana: data.type === 'MAGE' ? toNumber(data.mana) : null,
        magicDamage: data.type === 'MAGE' ? toNumber(data.magicDamage) : null
    };
    try {
        await api('/api/fighters', {method: 'POST', body: JSON.stringify(body)});
        $('formError').textContent = '';
        form.reset();
        toggleMageFields();
        await loadFighters();
    } catch (error) {
        $('formError').textContent = error.message;
    }
}

async function deleteFighter(id) {
    await api(`/api/fighters/${id}`, {method: 'DELETE'});
    await loadFighters();
}

function toggleMageFields() {
    const isMage = $('fighterForm').elements.type.value === 'MAGE';
    document.querySelectorAll('.mage-only').forEach(input => input.hidden = !isMage);
}

$('fightButton').addEventListener('click', fight);
$('fighterForm').addEventListener('submit', createFighter);
$('fighterForm').elements.type.addEventListener('change', toggleMageFields);
$('fighterTable').addEventListener('click', event => {
    const id = event.target.dataset.delete;
    if (id) deleteFighter(id);
});

toggleMageFields();
loadFighters();
loadHistory();
