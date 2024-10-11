export default function sleep(n = 5_000) {
  return new Promise(r => {
    setTimeout(r, n);
  });
}
