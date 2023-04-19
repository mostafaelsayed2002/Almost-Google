import { Html, Head, Main, NextScript } from "next/document";
export const metadata = {
  viewport: {
    width: "device-width",
    height: "device-height",
    initialScale: 1,
  },
};
export default function Document() {
  return (
    <Html lang="en">
      <Head></Head>
      <body className="">
        <Main />
        <NextScript />
      </body>
    </Html>
  );
}
