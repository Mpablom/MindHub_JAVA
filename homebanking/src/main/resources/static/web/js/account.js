Vue.createApp({
    data() {
        return {
            accountInfo: {},
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get(`/api/accounts/${id}`)
                .then((response) => {
                    // Obtener la información de la cuenta
                    this.accountInfo = response.data;
                    // Ordenar las transacciones por ID descendente
                    this.accountInfo.transactions.sort((a, b) => (b.id - a.id));
                    // Filtrar las cuentas activas
                    this.filterActiveAccounts();
                })
                .catch((error) => {
                    // Manejar errores
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                });
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        calculateCurrentBalance: function () {
            let currentBalance = 0;
            for (const transaction of this.accountInfo.transactions) {
                currentBalance += transaction.amount;
                transaction.currentBalance = currentBalance;
            }
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                });
        },
        desactivateAccount: function () {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');

            // Mostrar un mensaje de confirmación
            const isConfirmed = window.confirm("Are you sure you want to deactivate this account?");

            if (isConfirmed) {
                axios.delete(`/api/accounts/${id}`)
                    .then((response) => {
                        if (response.status === 200) {
                            window.location.href = "/web/accounts.html";
                            alert("Account deactivated successfully");
                        }
                    })
                    .catch((error) => {
                        console.error("Error deactivating the account: ", error);
                        alert("Error deactivating the account: " + error.response.data);
                    });
            }
        },
        filterActiveAccounts: function () {
            // Filtrar las cuentas activas
            this.accountInfo.transactions = this.accountInfo.transactions.filter(transaction => transaction.active);
            console.log("Transacciones activas:", this.accountInfo.transactions);
        },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app');
