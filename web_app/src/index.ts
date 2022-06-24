import 'svelte'
import App from './App.svelte'

function main() {
    let app_el = document.getElementById('app')!

    app_el.innerHTML = ''

    let app = new App({
        target: app_el,
    })

    console.log('baba')
}

setTimeout(main, 1);    // execute main() at startup
