<template>
  <div>
    <div style="height: 1rem"></div>
    <div class="container">
      <div class="reservation-top">
        <img src="@/assets/img/imgtemp.jpg" alt="" class="profile-img" />
        <div class="reservation-info">
          <div
            class="info"
            style="font-size: 1.3rem; display: flex; height: 2.3rem"
          >
            <b>예약 번호 : </b>
            <div style="width: 1rem"></div>
            <p>{{ reservationSeq }}</p>
          </div>
          <div
            class="info"
            style="font-size: 1.3rem; display: flex; height: 2.3rem"
          >
            <b>예약 시각 : </b>
            <div style="width: 1rem"></div>
            <p>{{ reservationDateTime }}</p>
          </div>
        </div>
        <div class="button-wrapper">
          <button class="consultant-mylist-enter" @click="startMeeting">
            입장하기
          </button>
          <button class="consultant-mylist-cancel">예약취소</button>
        </div>
      </div>
      <div style="height: 3rem"></div>
      <div style="margin-top: 1rem; margin-bottom: 3rem">
        <h4><b>예약자 정보</b></h4>
        <hr />
      </div>
      <div class="reservation-bottom" align="left" style="">
        <div class="info-detail" style="margin-top: 1rem; margin-bottom: 3rem">
          <h5><b>나이</b></h5>
          <p>{{ memberAge }}</p>
          <h5><b>성별</b></h5>
          <p>{{ memberGender }}</p>
          <h5><b>신장</b></h5>
          <p>{{ memberHeight }} cm</p>
          <h5><b>몸무게</b></h5>
          <p>{{ memberWeight }} kg</p>
          <h5><b>퍼스널컬러</b></h5>
          <p>{{ memberPersonalColor }}</p>
        </div>
      </div>
      <hr />

      <div style="height: 3rem"></div>
      <div>
        <h4 style="margin-top: 10px"><b>예약자가 등록한 이미지</b></h4>
        <div class="reservation-bottom">
          <div class="image-list" v-if="memberImages.length >= 1">
            <div
              class="image-item"
              v-for="(image, index) in memberImages"
              :key="index"
            >
              <img :src="image.imageUrl" :alt="image.alt" class="image" />
            </div>
          </div>
          <div v-else>
            <div style="height: 2rem"></div>
            이미지를 등록해주세요!
            <div style="height: 2rem"></div>
          </div>
        </div>
        <hr />
      </div>
      <div style="height: 3rem"></div>
      <div>
        <h4 style="margin-top: 10px"><b>컨설턴트가 등록한 이미지</b></h4>
        <div class="image-save" v-if="consultantImages.length === 0">
          <multi-image-upload @updateImg="updateImg"></multi-image-upload>
          <div style="text-align: end">
            <button type="button" class="active-button" @click="submitPost">
              <span>등록</span>
            </button>
          </div>
        </div>
        <div v-if="consultantImages.length >= 1" class="image-list">
          <div
            class="image-item"
            v-for="(image, index) in consultantImages"
            :key="index"
          >
            <img :src="image.imageUrl" :alt="image.alt" class="image" />
          </div>
        </div>
        <hr />
      </div>
    </div>
    <div style="height: 5rem"></div>
  </div>
</template>
<style scoped>
.container {
  width: 80%;
}
.reservation-top {
  display: flex;
  justify-content: center;
  align-items: center;
}
.profile-img {
  flex: 1;
  max-width: 20vh;
  max-height: 20vh;
  margin: 10px;
  border-radius: 50%;
  margin-right: 3rem;
}
.reservation-info {
  flex: 2;
}

.button-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.info {
  text-align: left;
}
.image-list {
  margin: auto;
  display: flex;
  max-width: 60vw;
  overflow-x: auto;
}
.image-item {
  border: 1px solid #ccc;
  flex: 0 0 auto;
  margin-right: 10px;
}
.image {
  max-width: 25vh;
  max-height: 25vh;
}
.consultant-mylist-enter {
  background-color: #ed4141;
  color: white;
  border-radius: 10px;
  padding: 10px;
  width: 100px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 0.4rem;
}
.consultant-mylist-cancel {
  background-color: #bdbdbd;
  color: white;
  border-radius: 10px;
  padding: 10px;
  width: 100px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

<script>
import axios from "axios";
import MultiImageUpload from "../../shared/MultiImageUpload.vue";
import router from "@/router";

export default {
  name: "RConsultantCheckDetail",
  props: ["value"],
  created() {
    const reservationSeq = this.value;
    const nickname = this.$store.getters["memberStore/checkLoginUser"].nickname;
    const token = sessionStorage.getItem("token");

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/${nickname}/reservations/${reservationSeq}`,
      headers: { Authorization: `Bearer ${token}` },
      method: "GET",
    })
      .then(({ data }) => {
        let details = data.consultantReservationDetails[0];
        this.reservationSeq = data.reservationSeq;
        this.consultantNickname = details.consultantNickname;
        this.memberNickname = details.memberNickname;
        if (details.personalColor === null) {
          this.personalColor = "퍼스널컬러를 등록해주세요!";
        } else {
          this.personalColor = details.personalColor;
        }
        this.memberPersonalColor = details.personalColor;
        if (details.gender === "MALE") {
          this.memberGender = "남성";
        } else if (details.gender === "FEMALE") {
          this.memberGender = "여성";
        }
        if (details.height === null) {
          this.memberHeight = "신장을 등록해주세요!";
        } else {
          this.memberHeight = details.height;
        }
        if (details.weight === null) {
          this.memberWeight = "몸무게를 등록해주세요!";
        } else {
          this.memberWeight = details.weight;
        }
        if (details.age === null) {
          this.memberAge = "나이를 등록해주세요!";
        } else {
          this.memberAge = details.age;
        }
        const dateTime = details.reservationDateTime;
        this.reservationDateTime = `${dateTime[0]}년 ${dateTime[1]}월 ${dateTime[2]}일 ${dateTime[3]}시`;
        this.memberImages = details.memberImages;
        this.consultantImages = details.consultantImages;
      })
      .catch((error) => {
        console.log(error);
      });
  },
  data() {
    return {
      reservationSeq: null,
      memberNickname: null,
      consultantNickname: null,
      memberPersonalColor: null,
      memberGender: null,
      memberHeight: null,
      memberWeight: null,
      memberAge: null,
      reservationDateTime: null,
      reservationDetail: null,
      memberImages: [],
      consultantImages: [],
      fileList: [],
      imgList: [],
    };
  },
  methods: {
    startMeeting() {
      const sessionId = this.reservationSeq + 73576;
      const array = [...this.memberImages, ...this.consultantImages];
      const imageList = [];
      for (let i = 0; i < array.length; i++) {
        imageList.push(array[i].imageUrl);
      }
      this.$router.push({
        name: "Consulting-WebCam-View",
        query: { sessionId, imageList },
      });
    },
    async submitPost() {
      const saveData = {
        images: this.fileList,
        reservationSeq: this.reservationSeq,
      };
      await this.callImageSaveAPI(saveData);
      router.go();
    },
    async callImageSaveAPI(saveData) {
      let formData = new FormData();
      for (let i = 0; i < saveData.images.length; i++) {
        formData.append("images", saveData.images[i]);
      }
      formData.append("reservationSeq", saveData.reservationSeq);
      var token = sessionStorage.getItem("token");
      await axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/reservation`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
                "Content-Type": "multipart/form-data",
              },
        method: "PATCH",
        data: formData,
      })
        .then(() => {})
        .catch(() => {
          alert("사진이 등록되지 않았습니다.");
        });
    },
    updateImg(file) {
      this.fileList = file;
    },
  },
  components: {
    MultiImageUpload,
  },
};
</script>

<style scoped></style>
