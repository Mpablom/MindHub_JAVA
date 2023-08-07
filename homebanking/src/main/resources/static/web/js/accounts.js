Vue.createApp({
    data() {
        return {
            clientInfo: {},
            errorToats: null,
            errorMsg: null,
            theme:"light",
        }
    },
    methods: {
        getData() {
            axios.get("/api/clients/1")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-us');
        },
        toggleTheme() {
            console.log("cambio de tema");
            const body = document.querySelector('body');
            if (this.theme === "dark") {
                body.classList.remove("dark-theme");
                body.classList.add("light-theme");
                this.theme = "light";
            }else{
                body.classList.remove("light-theme");
                body.classList.add("dark-theme");
                this.theme = "dark";
            }
        },
    },
    mounted() {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app');