import React from 'react'

function TickerSearchItem(props) {
  return (
    <div key={props.ticker} className="card card-body mb-1">
      <small key={props.ticker} 
             ticker={props.ticker}
             onClick={function(e){
               console.log('onClick item >>> ' + props.ticker);
              //  this.props.onClick(props.ticker);
             }.bind(this)}
             >
             {/* onClick={function(e){console.log(props.ticker)}.bind(this)}> */}
          회사명 : {props.companyName} / 종목코드 : {props.ticker}
      </small>
    </div>
  );
}

export default TickerSearchItem