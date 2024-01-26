import {useRecoilValue} from "recoil";
import {userInfoState} from "../state.js";

function MainPage() {
  const userInfo = useRecoilValue(userInfoState)

  console.log(userInfo)

  return (
      <>
        {userInfo.jwt}
      </>
  )
}

export default MainPage
