import logo from './logo.svg';
import './App.css';
import React, {useState} from 'react';

function App() {
  return (
    <div className="container">
      <h1>컴포넌트 state 를 클래스/함수형 스타일로 구현해보기</h1>
      <ClassStyleComponent fruitType={"APPLE"}></ClassStyleComponent>
      <FunctionalComponent fruitType={"BANANA"}></FunctionalComponent>
    </div>
  );
}

class ClassStyleComponent extends React.Component{
  state = {
    fruitType : this.props.fruitType
  };

  render(){
    return (
      <div className="container">
        <h2>클래스 형 컴포넌트 : {this.state.fruitType}</h2>
        <input type="text" onChange={function(e){
          this.setState({
            fruitType: e.target.value
          });
        }.bind(this)}/>
      </div>
    );
  }
}

function FunctionalComponent(props){
  var fruitTypeProps = useState(props.fruitType);
  var fruitType = fruitTypeProps[0];    // 0 번째 요소는 state 변수
  var setFruitType = fruitTypeProps[1]; // 1 번째 요소는 setter 함수 

  return (
    <div className="container">
      <h2>함수형 컴포넌트 : {fruitType}</h2>
      <input type="text" onChange={function(e){
        setFruitType(e.target.value);
      }.bind(this)}/>
    </div>
  );
}



export default App;
