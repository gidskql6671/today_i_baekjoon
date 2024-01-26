import Header from "./components/Header.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import styled from "styled-components";
import {useRecoilValue} from "recoil";
import {userInfoState} from "./state.js";
import MainPage from "./pages/MainPage.jsx";
import {Navigate, Route, Routes, useLocation, useNavigate} from "react-router-dom";
import LoginCallback from "./components/LoginCallback.jsx";
import {useEffect} from "react";


const ContentContainer = styled.div`
  padding: 10px;
	display: flex;
  flex-direction: column;
	justify-content: space-between;
  align-items: center;
	width: 100%;
`;

const Router = ({userInfo}) => {
  if (userInfo == null) {
    return (
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/login/callback" element={<LoginCallback />} />
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
    )
  }
  else {
    return (
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    )
  }
}


function App() {
  const userInfo = useRecoilValue(userInfoState)
  const navigate = useNavigate()
  const location = useLocation()

  useEffect(() => {
    if (!location.pathname.includes('/login') && userInfo == null) {
      navigate('/login', { replace: true })
    }
  }, [userInfo, location, navigate]);

  return (
    <>
      <Header />
      <ContentContainer>
        <Router userInfo={userInfo} />
      </ContentContainer>
    </>
  )
}

export default App
