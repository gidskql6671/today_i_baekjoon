import {useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import qs from "qs";
import {useSetRecoilState} from "recoil";
import {userInfoState} from "../state.js";


const LoginCallback = () => {
  const setUserInfo = useSetRecoilState(userInfoState)
  const navigate = useNavigate()
  const location = useLocation()

  const authPath = `${import.meta.env.VITE_SERVER_DOMAIN}/api/login`;

  useEffect(() => {
    const getToken = async () => {
      const { code } = qs.parse(location.search, {
        ignoreQueryPrefix: true,
      });

      try {
        // const response = await fetch(`${authPath}?code=${code}`);
        // const data = await response.json();
        const data = {jwt: 'test', avatar_url: 'test'}

        setUserInfo({
          jwt: data.jwt,
          profileUrl: data.avatar_url
        })

        navigate('/', { replace: true });
      } catch (error) {
        console.error(error)
      }
    };

    getToken();
  }, []);
}

export default LoginCallback