Vue.createApp({
    data() {
        return {
            clientInfo: {},
            errorToats: null,
            errorMsg: null,
            maxAccountsToShow: 3, // Establece el número máximo de cuentas a mostrar
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    // Filtra las cuentas activas antes de asignarlas a clientInfo.accounts
                    this.clientInfo.accounts = response.data.accounts.filter(account => account.active);
                    // Limita el número de cuentas a mostrar
                    this.clientInfo.accounts = this.clientInfo.accounts.slice(0, this.maxAccountsToShow);
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        create: function () {
            axios.post('/api/clients/current/accounts')
                .then(response => {
                    // Actualizar la vista volviendo a cargar la información del cliente
                    this.getData();
                })
                .catch((error) => {
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')
