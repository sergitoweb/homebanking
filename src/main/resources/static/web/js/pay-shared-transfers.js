var app = new Vue({
    el:"#app",
    data:{
        clientAccounts: [],
        sharedTransfer: {},
        tokenId: 0,
        errorToats: null,
        errorMsg: null,
        accountFromNumber: "VIN",
        accountToNumber: "VIN",
        amount: 0,
        totalAmount: 0,
        description: ""
    },
    methods:{
        getData: function(){

            axios.get("/api/clients/current/accounts")
            .then((response) => {
                this.clientAccounts = response.data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })

        },
        getSharedTransferData: function(){
                    const urlParams = new URLSearchParams(window.location.search);
                    const tokenId = urlParams.get('tokenId');
                    this.tokenId= tokenId;
                    axios.get(`/api/transactions/shared/token/${tokenId}`)
                    .then((response) => {
                        //get client ifo
                        this.sharedTransfer = response.data;
                        this.cargarDatos();
                        console.log(this.sharedTransfer);
                    })
                    .catch((error) => {
                        this.errorMsg = "Error getting data";
                        this.errorToats.show();
                    })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkTransfer: function(){
            if(this.accountFromNumber == "VIN"){
                this.errorMsg = "You must select an origin account";
                this.errorToats.show();
            }
            else if(this.accountToNumber == "VIN"){
                this.errorMsg = "You must select a destination account";
                this.errorToats.show();

            }else if(this.amount == 0){
                this.errorMsg = "You must indicate an amount";
                this.errorToats.show();
            }
            else if(this.description.length <= 0){
                this.errorMsg = "You must indicate a description";
                this.errorToats.show();
            }else{
                this.modal.show();
            }
        },
        transfer: function(){
            let config = {
            }
            axios.post(`/api/transactions?fromAccountNumber=${this.accountFromNumber}&toAccountNumber=${this.accountToNumber}&amount=${this.amount}&description=${this.description}`)
            .then(response => {
                this.modal.hide();
                this.okmodal.show();
            })
            .catch((error) =>{
                this.errorMsg = error.response.data;
                this.errorToats.show();
            })
        },
        changedType: function(){
            this.accountFromNumber = "VIN";
            this.accountToNumber = "VIN";
        },
        finish: function(){
            window.location.reload();
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"
                this.errorToats.show();
            })
        },
        cargarDatos: function(){
            this.accountToNumber= this.sharedTransfer.toAccount;
            this.amount= this.sharedTransfer.parcialAmount;
            this.totalAmount = this.sharedTransfer.totalAmount;
            this.description= this.sharedTransfer.description;
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getData();
        this.getSharedTransferData();
    }
})