<template lang="">
  <div
    class="row justify-content-center"
    style="
      margin-bottom: 30px;
      border-style: solid;
      border-width: 1px;
      background-color: white;
      margin-top: -20px;
      border-color: white white #bdbdbd white;
    "
  >
    <div class="col col-lg-2 header-tab point">
      <router-link to="/post" style="text-decoration: none; color: #424242"
        >Home</router-link
      >
    </div>
    <div class="col col-lg-2 header-tab">
      <router-link
        v-if="isConsultant"
        to="/consultant/rc"
        style="text-decoration: none; color: #424242"
        >Consultant</router-link
      >

      <router-link
        v-else
        to="/consultant/rm"
        style="text-decoration: none; color: #424242"
        >Consultant</router-link
      >
    </div>

    <div class="col col-lg-2 header-tab">
      <router-link
        :to="profileLink"
        style="text-decoration: none; color: #424242"
        >Mypage</router-link
      >
    </div>
  </div>
</template>
<script>
export default {
  data() {
    let isConsultant = false;
    if (this.$store.getters["memberStore/checkLoginUser"] !== null) {
      const roles =
        this.$store.getters["memberStore/checkLoginUser"].memberRole;

      for (let i = 0; i < roles.length; i++) {
        if (roles[i] === "CONSULTANT") isConsultant = true;
      }
    }

    return {
      isConsultant: isConsultant,
      myNickname: null,
    };
  },
  computed: {
    profileLink() {
      return `/profile/${this.myNickname}`;
    },
  },
  created() {
    if (this.$store.getters["memberStore/checkLoginUser"] !== null) {
      this.myNickname =
        this.$store.getters["memberStore/checkLoginUser"].nickname;
    }
  },
};
</script>
<style scoped>
* {
  font-size: 25px;
}
.point {
  font-weight: bold;
}
</style>
