<template>
  <span :title="props.minutes + ' Minuten'">
    {{ parsed.hours }}:{{ parsed.minutes }}h
  </span>
</template>

<script setup>
import {computed} from "vue";

const props = defineProps(['minutes'])

const parsed = computed(() => {
  let hours = Math.floor(props.minutes / 60)
  let minutes = props.minutes - (hours * 60)
  return {hours: twoDigit(hours), minutes: twoDigit(minutes)}
})

function twoDigit(num) {
  let s = num.toString();
  return (s.length === 1) ? ('0' + s) : s
}
</script>
