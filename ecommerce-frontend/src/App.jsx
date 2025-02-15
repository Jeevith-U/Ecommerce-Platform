import { useState } from 'react'
import { IoAccessibility } from "react-icons/io5";
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div className='bg-gray-700 flex items-center justify-center' >
        <h1>Hello This is Jeevith</h1> 
        <IoAccessibility/>
      </div>
    </>
  )
}

export default App
