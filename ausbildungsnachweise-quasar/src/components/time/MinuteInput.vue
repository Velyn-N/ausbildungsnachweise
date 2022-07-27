<template>
  <q-input v-model="displayVal" mask="time" placeholder="00:00" reverse-fill-mask maxlength="5" @focusin="displayVal = ''" />
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';

const props = defineProps({modelValue: Number})
const emit = defineEmits(['update:modelValue'])
const emitValue = (value) => emit('update:modelValue', value)

const displayVal = ref('')

watch(displayVal, async (newVal) => {
  let parts = newVal.split(":")
  let hours = +parts[0]
  let minutes = +parts[1]

  if ((0 <= hours <= 24) && (0 <= minutes <= 59)) {

  }

  let sum = (hours * 60) + minutes
  emitValue(isNaN(sum) ? 0 : sum)
})

onMounted(() => {
  let hours = Math.floor(props.modelValue / 60)
  let minutes = props.modelValue - (hours * 60)
  displayVal.value = twoDigit(hours) + ':' + twoDigit(minutes)
})

function twoDigit(num) {
  let s = num.toString();
  return (s.length === 1) ? ('0' + s) : s
}
</script>
