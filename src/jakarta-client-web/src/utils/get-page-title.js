import defaultSettings from '../settings'

const title = defaultSettings.title || 'Jakarta'

export default function getPageTitle (pageTitle) {
  if (pageTitle) {
    return `${title} > ${pageTitle}`
  }
  return `${title}`
}
