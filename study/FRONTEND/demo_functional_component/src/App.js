import logo from './logo.svg';
import './App.css';
import React from 'react';

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
  render(){
    return (
      <div className="container">
        <h2>클래스 형 컴포넌트 : {this.props.fruitType}</h2>
      </div>
    );
  }
}

function FunctionalComponent(props){
  return (
    <div className="container">
      <h2>함수형 컴포넌트 : {props.fruitType}</h2>
    </div>
  );
}



export default App;
