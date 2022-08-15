<template>
    <div class="navbar">
        <div class="avatars">
            Avatar placeholder
        </div>
        <div class="menu">
            Menu:<br>
            <button @click="toggleForm()" id="about" class="about">About</button>
            <button @click="toggleForm()" id="showEmailForm" class="showEmailForm">Email</button>
            <button @click="toggleForm()" id="help" class="help">Help</button>
            <button id="interview" class="interview">Interview</button>
        </div>
    </div>
</template>

<script>
export default {

}
</script>

<style>
.navbar {
    display: grid;
    background-color: BFC0C0;
    grid-template-rows:  .5fr, .5fr, 8fr;
    grid-template-areas: 
        "."
        "avatar"
        "menu";
}

.avatars {
    grid-area: avatar;
}

.menu {
    grid-area: menu;
    display: flex;
    flex-direction: column;
}
</style>