export class  Balance{
    private hufShow : number
    private eurShow : number
    private hufCard : number
    private eurCard : number
    private hufCash : number
    private eurCash : number
  
    hufTotal(){
      return this.hufCard+this.hufCash
    }
  
    eurTotal(){
      return this.eurCard+this.eurCash
    }
    set(response : any[]){
      response.forEach(element => {
        switch(element.currency){
          case "HUF":
          this.hufCard = element.currentCardBalance
          this.hufCash = element.currentCashBalance
          this.hufShow = this.hufTotal()
          break
  
          case "EUR":
          this.eurCard = element.currentCardBalance
          this.eurCash = element.currentCashBalance
          this.eurShow = this.eurTotal()
          break
        }
      });
    }
  
    showCash(){
      this.eurShow = this.eurCash
      this.hufShow = this.hufCash
    }
    showCard(){
      this.eurShow = this.eurCard
      this.hufShow = this.hufCard
    }
    showTotal(){
      this.eurShow = this.eurTotal()
      this.hufShow = this.hufTotal()
    }
  }
  