import defaultSettings from '@/settings'

const title = defaultSettings.title || 'The JaBack'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
