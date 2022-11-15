var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        errorToats: null,
        errorMsg: null,
        type:null,
        typeMoney:null,
        options: []
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;

            })
            .catch((error)=>{
                // handle error
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"   
                this.errorToats.show();
            })
        },
        create: function(){
            var url= '/api/clients/current/accounts/' +this.type+","+this.typeMoney;

            axios.post(url)
            .then(response => window.location.reload())
            .catch((error) =>{
                this.errorMsg = error.response.data;  
                this.errorToats.show();
            })
        },

       changeItem: function changeItem(rowId, event) {

             if(event.target.value == "VIN"){
                this.options= [
                                           { text: 'ARS', value: 'ARS' }

                                         ]
             }else{

                this.options= [
                           { text: 'BTC', value: 'BTC' },
                           { text: 'DAI', value: 'DAI' },
                           { text: 'USDT', value: 'USDT' }
                         ]
            }
          }

    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})
