<script lang="ts">
    import RollsView from './Rolls.svelte'
    import SettingsView from './Settings.svelte'

    var view = RollsView

    function go_rolls() {
        view = RollsView
    }

    function go_settings() {
        view = SettingsView
    }
</script>

<nav class="top-menu">
    <ul>
        <li><button on:click={go_rolls}>Rolls</button></li>
        <li><button on:click={go_settings}>Settings</button></li>
    </ul>
</nav>

<svelte:component this={view}></svelte:component>

<style lang="scss">
</style>
