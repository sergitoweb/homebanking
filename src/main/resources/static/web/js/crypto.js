var app = new Vue({
    el:"#app",
    data:{
        clientAccounts: [],
        clientAccountsTo: [],
        debitCards: [],
        errorToats: null,
        errorMsg: null,
        criptoAccount: '',
        currentAccount: '',
        operation: "",

        labelAmount: "Elige una operacion",
        amount: 0,
        typeMoney:{},
        typecripto: {},
        cryptoamount:'',
        currentamount:'',
        listacripto:[]
    },
    methods:{
        getData: function(){

            axios.get("/api/clients/current/accounts")
            .then((response) => {
                //get client ifo
                this.clientAccounts = response.data;
            })
            .then(() => {
                         axios.get(`/api/crypto`)
                                    .then((response) => {
                                        this.listacripto = response.data;
                                    })
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
            }else{
                this.modal.show();

            }
        },
        changeLabel : function(label){
            this.labelAmount = label;
        },
        transfer: function(){

            if(this.operation == "buy"){//controller buycrypto
               // this.labelAmount="Enter your amount in ARS";
                axios.post(`/api/crypto/buy?amountArsBuy=${this.amount}&tipomoneda=${this.typecripto.name}&originAccount=${this.currentAccount}&destinationAccount=${this.criptoAccount}`)
                .then(response => {
                    this.modal.hide();
                    this.okmodal.show();
                })
                .catch((error) =>{
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })

            }else{ //controller sellcripto
                //this.labelAmount="Enter your amount in COIN";
                console.log(this.criptoAccount +" - "+this.currentAccount)
                                axios.post(`/api/crypto/sell?amountCriptoSell=${this.amount}&tipomoneda=${this.typecripto.name}&originAccount=${this.criptoAccount}&destinationAccount=${this.currentAccount}`)
                                .then(response => {
                                    this.modal.hide();
                                    this.okmodal.show();
                                })
                                .catch((error) =>{
                                    this.errorMsg = error.response.data;
                                    this.errorToats.show();
                                })
            }
        },
        changedType: function(){
            this.accountFromNumber = "VIN";
            this.accountToNumber = "VIN";
        },
        changedFrom: function(){
            if(this.trasnferType == "own"){
                this.clientAccountsTo = this.clientAccounts.filter(account => account.number != this.accountFromNumber);
                this.accountToNumber = "VIN";
            }
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
        updateParams: function(){
            let urlParams = new URLSearchParams(window.location.search);
                if (urlParams.get('amount')) {
                    this.amount = urlParams.get('amount');
                };
                if (urlParams.get('description')) {
                    this.description = urlParams.get('description');
                }
                if (urlParams.get('type')) {
                    this.trasnferType = urlParams.get('type');
                }
                if (urlParams.get('accountNumber')) {
                    this.accountToNumber = urlParams.get('accountNumber');
                }

        },
            simulator: function(){
                   if(this.cryptoamount!=''){//convertir de cripto ars

                      this.currentamount = this.cryptoamount * this.typeMoney.totalAsk;
                      this.cryptoamount='';

                   }else{ // convertir de ars a cripto

                        this.cryptoamount= this.currentamount / this.typeMoney.totalBid;
                        currentamount='';
                   };
            }

    },

    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getData();
        //this.updateParams();
    }

})