import { useEffect, useState } from 'react';

export const useShareRoom = () => {
  const [copied, setCopied] = useState(false);

  useEffect(() => {
    if (copied)
      window.setTimeout(() => {
        setCopied(false);
      }, 3000);
  }, [copied]);

  const share = (url: string) => {
    const shareData = {
      title: "Dołącz do pokoju w MamyKlocga",
      url,
    }

    if (typeof navigator.share != "function" || !navigator.canShare(shareData)) {
      window.navigator.clipboard.writeText(url).then(() => {
        setCopied(true)
      })
      return
    }

    navigator.share(shareData).then() // Only over HTTP
  }

  return { share, copied }
}